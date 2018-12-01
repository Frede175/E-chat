using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Server.Context;
using System.Threading.Tasks;
using Microsoft.Extensions.Logging;
using AspNet.Security.OpenIdConnect.Primitives;
using OpenIddict.Server;
using Microsoft.AspNetCore.Authentication;
using AspNet.Security.OpenIdConnect.Extensions;
using OpenIddict.Abstractions;
using System.Linq;
using System.Collections.Generic;
using Microsoft.Extensions.Options;
using Server.Logging;

namespace Server.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {

        private readonly SignInManager<ApplicationUser> _signInManager;

        private readonly UserManager<ApplicationUser> _userManager;

        private readonly ILogger<AuthController> _logger;

        private readonly IOptions<IdentityOptions> _identityOptions;

        public AuthController(SignInManager<ApplicationUser> signInManager,
            UserManager<ApplicationUser> userMananger,
            ILogger<AuthController> logger,
            IOptions<IdentityOptions> options)
        {
            _signInManager = signInManager;
            _userManager = userMananger;
            _logger = logger;
            _identityOptions = options;
        }

        [HttpPost("~/connect/logout")]
        [Authorize]
        public async Task<IActionResult> Logout() {
            var username = _userManager.GetUserName(HttpContext.User);
            _logger.LogInformation(LoggingEvents.SignOut, $"{username} signed out.");
            await _signInManager.SignOutAsync();
            
            return SignOut(OpenIddictServerDefaults.AuthenticationScheme);
        }

        [HttpPost("~/connect/token"), Produces("application/json")]
        public async Task<IActionResult> Exchange(OpenIdConnectRequest oidcRequest)
        {
            _logger.LogInformation(LoggingEvents.Get, "Exchange started for {username}", oidcRequest.Username);
            if (oidcRequest.IsPasswordGrantType())
            {
                var user = await _userManager.FindByNameAsync(oidcRequest.Username);
                if (user == null)
                {
                    _logger.LogWarning(LoggingEvents.AuthenticationFail, $"User not found for {oidcRequest.Username}");
                    return BadRequest(new OpenIdConnectResponse
                    {
                        Error = OpenIdConnectConstants.Errors.InvalidGrant,
                        ErrorDescription = "The username/password couple is invalid."
                    });
                }

                // Validate the username/password parameters and ensure the account is not locked out.
                var result = await _signInManager.CheckPasswordSignInAsync(user, oidcRequest.Password, lockoutOnFailure: true);
                if (!result.Succeeded)
                {
                    _logger.LogWarning(LoggingEvents.AuthenticationFail, $"Wrong password for {oidcRequest.Username}");
                    return BadRequest(new OpenIdConnectResponse
                    {
                        Error = OpenIdConnectConstants.Errors.InvalidGrant,
                        ErrorDescription = "The username/password couple is invalid."
                    });
                }

                // Create a new authentication ticket.
                var ticket = await CreateTicketAsync(oidcRequest, user);

                _logger.LogInformation(LoggingEvents.AuthenticationSuccess, $"Authentication successful for {oidcRequest.Username}");

                return SignIn(ticket.Principal, ticket.Properties, ticket.AuthenticationScheme);
            }
            _logger.LogInformation(LoggingEvents.AuthenticationFail, $"Unsupported grant type for {oidcRequest.Username}");
            return BadRequest(new OpenIdConnectResponse
            {
                Error = OpenIdConnectConstants.Errors.UnsupportedGrantType,
                ErrorDescription = "The specified grant type is not supported."
            });
        }

        private async Task<AuthenticationTicket> CreateTicketAsync(
            OpenIdConnectRequest oidcRequest, ApplicationUser user,
            AuthenticationProperties properties = null)
        {
            // Create a new ClaimsPrincipal containing the claims that
            // will be used to create an id_token, a token or a code.
            var principal = await _signInManager.CreateUserPrincipalAsync(user);

            // Create a new authentication ticket holding the user identity.
            var ticket = new AuthenticationTicket(principal, properties,
                OpenIddictServerDefaults.AuthenticationScheme);

            if (!oidcRequest.IsAuthorizationCodeGrantType())
            {
                // Set the list of scopes granted to the client application.
                // Note: the offline_access scope must be granted
                // to allow OpenIddict to return a refresh token.
                ticket.SetScopes(new[]
                {
                    OpenIdConnectConstants.Scopes.OpenId,
                    OpenIdConnectConstants.Scopes.Email,
                    OpenIdConnectConstants.Scopes.Profile,
                    OpenIdConnectConstants.Scopes.OfflineAccess,
                    OpenIddictConstants.Scopes.Roles
                }.Intersect(oidcRequest.GetScopes()));
            }

            ticket.SetResources("resource_server");

            // Note: by default, claims are NOT automatically included in the access and identity tokens.
            // To allow OpenIddict to serialize them, you must attach them a destination, that specifies
            // whether they should be included in access tokens, in identity tokens or in both.

            foreach (var claim in ticket.Principal.Claims)
            {
                // Never include the security stamp in the access and identity tokens, as it's a secret value.
                if (claim.Type == _identityOptions.Value.ClaimsIdentity.SecurityStampClaimType)
                {
                    continue;
                }

                var destinations = new List<string>
                {
                    OpenIdConnectConstants.Destinations.AccessToken
                };

                // Only add the iterated claim to the id_token if the corresponding scope was granted to the client application.
                // The other claims will only be added to the access_token, which is encrypted when using the default format.
                if ((claim.Type == OpenIdConnectConstants.Claims.Name && ticket.HasScope(OpenIdConnectConstants.Scopes.Profile)) ||
                    (claim.Type == OpenIdConnectConstants.Claims.Email && ticket.HasScope(OpenIdConnectConstants.Scopes.Email)) ||
                    (claim.Type == OpenIdConnectConstants.Claims.Role && ticket.HasScope(OpenIddictConstants.Claims.Roles)))
                {
                    destinations.Add(OpenIdConnectConstants.Destinations.IdentityToken);
                }

                claim.SetDestinations(destinations);
            }

            return ticket;
        }
    }

}