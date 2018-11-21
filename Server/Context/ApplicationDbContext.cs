using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using Server.DbModels;

namespace Server.Context
{
    public class ApplicationDbContext : IdentityDbContext<ApplicationUser>
    {
        
        public DbSet<Chat> Chat { get; set; }
        public DbSet<Message> Message { get; set; }
        public DbSet<Department> Department { get; set; }

        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
            : base(options)
        {
        }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);

            builder.Entity<UserChat>().HasKey(u => new { u.ChatId, u.UserId });
            builder.Entity<UserDepartment>().HasKey(u => new { u.UserId, u.DepartmentId });
            
            builder.Entity<UserChat>()
                .HasOne(u => u.ApplicationUser)
                .WithMany(u => u.UserChats)
                .HasForeignKey(u => u.UserId);

            builder.Entity<UserChat>()
                .HasOne(u => u.Chat)
                .WithMany(u => u.UserChats)
                .HasForeignKey(u => u.ChatId);

            builder.Entity<UserDepartment>()
                .HasOne(u => u.ApplicationUser)
                .WithMany(u => u.UserDepartments)
                .HasForeignKey(u => u.UserId);

            builder.Entity<UserDepartment>()
                .HasOne(u => u.Department)
                .WithMany(u => u.UserDepartments)
                .HasForeignKey(u => u.DepartmentId);

            builder.Entity<Chat>()
                .HasOne(c => c.Department)
                .WithMany(d => d.Chats)
                .OnDelete(DeleteBehavior.Cascade);
        }


    }
}