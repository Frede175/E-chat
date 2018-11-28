using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.AzureADB2C.UI;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using Server.Hubs;
using Server.Context;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using System.Security.Cryptography.X509Certificates;
using Microsoft.AspNetCore.Identity;
using System.IdentityModel.Tokens.Jwt;
using AspNet.Security.OpenIdConnect.Primitives;
using OpenIddict.Abstractions;
using OpenIddict.Validation;
using Server.Service.Interfaces;
using Server.Service;
using Microsoft.AspNetCore.Authorization;
using Server.Security;
using Newtonsoft.Json;
using Microsoft.AspNetCore.HttpOverrides;
using Server.Logging;

namespace Server 
{
    public class Startup 
    {
        public Startup(IConfiguration configuration) 
        {
            Configuration = configuration;
        }
        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services) 
        {


            if (Environment.GetEnvironmentVariable("ASPNETCORE_ENVIRONMENT") == "Production") 
            {
                services.AddDbContext<ApplicationDbContext>(options => 
                {
                    options.UseSqlServer(Configuration.GetConnectionString("Connection"));
                    options.UseOpenIddict();
                });
            } else 
            {
                services.AddEntityFrameworkNpgsql().AddDbContext<ApplicationDbContext>(options => 
                {
                    options.UseNpgsql(Configuration["DB:Connectionstring"]);
                    options.UseOpenIddict();
                });
            }




            services.BuildServiceProvider().GetService<ApplicationDbContext>().Database.Migrate();

            services.AddIdentity<ApplicationUser, IdentityRole>()
                .AddEntityFrameworkStores<ApplicationDbContext>()
                .AddDefaultTokenProviders();

            services.Configure<IdentityOptions>(options => 
            {
                options.ClaimsIdentity.UserNameClaimType = OpenIdConnectConstants.Claims.Name;
                options.ClaimsIdentity.UserIdClaimType = OpenIdConnectConstants.Claims.Subject;
                options.ClaimsIdentity.RoleClaimType = OpenIdConnectConstants.Claims.Role;
            });

            services.AddOpenIddict()

                // Register the OpenIddict core services.
                .AddCore(options => 
                {
                    // Register the Entity Framework stores and models.
                    options.UseEntityFrameworkCore()
                           .UseDbContext<ApplicationDbContext>();
                })

                // Register the OpenIddict server handler.
                .AddServer(options => 
                {
                    // Register the ASP.NET Core MVC binder used by OpenIddict.
                    options.UseMvc();

                    // Enable the token endpoint.
                    options.EnableTokenEndpoint("/connect/token")
                        .EnableLogoutEndpoint("/connect/logout")
                        .EnableUserinfoEndpoint("/api/userinfo");

                    // Enable the password flow.
                    options.AllowPasswordFlow();

                    // Accept anonymous clients (i.e clients that don't send a client_id).
                    options.AcceptAnonymousClients();

                    // During development, you can disable the HTTPS requirement.
                    options.DisableHttpsRequirement();
                })

                // Register the OpenIddict validation handler.
                .AddValidation();

            services.AddAuthentication(options => 
            {
                options.DefaultAuthenticateScheme = OpenIddictValidationDefaults.AuthenticationScheme;
                options.DefaultScheme = OpenIddictValidationDefaults.AuthenticationScheme;
                options.DefaultChallengeScheme = OpenIddictValidationDefaults.AuthenticationScheme;
            });


            services.AddScoped<IDepartmentService, DepartmentService>();
            services.AddScoped<IChatService, ChatService>();
            services.AddScoped<IMessageService, MessageService>();
            services.AddSingleton<IAuthorizationHandler, PermissionsAuthorizationHandler>();
            services.AddSingleton(typeof(IHubState<,>), typeof(HubState<,>));
            services.AddSingleton<IDbLoggingHandler, DbLoggingHandler>();

            services.AddMvc().SetCompatibilityVersion(CompatibilityVersion.Version_2_1).AddJsonOptions(options => 
            {
                options.SerializerSettings.DateTimeZoneHandling = DateTimeZoneHandling.Utc;
            });
            services.AddSignalR().AddJsonProtocol();


        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env) 
        {

            if (env.IsDevelopment()) 
            {
                app.UseDeveloperExceptionPage();
                app.UseHttpsRedirection();
            } 
            else 
            {
                app.UseHsts();
                app.UseForwardedHeaders(new ForwardedHeadersOptions 
                {
                    ForwardedHeaders = ForwardedHeaders.XForwardedFor | ForwardedHeaders.XForwardedProto
                });
            }

            app.UseSignalR(route => 
            {
                route.MapHub<ChatHub>("/hubs/chat");
            });


            app.UseAuthentication();
            app.UseMvc();
        }


    }
}
