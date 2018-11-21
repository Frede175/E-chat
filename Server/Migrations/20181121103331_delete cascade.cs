using Microsoft.EntityFrameworkCore.Migrations;

namespace Server.Migrations
{
    public partial class deletecascade : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Chat_Department_DepartmentId",
                table: "Chat");

            migrationBuilder.AddForeignKey(
                name: "FK_Chat_Department_DepartmentId",
                table: "Chat",
                column: "DepartmentId",
                principalTable: "Department",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Chat_Department_DepartmentId",
                table: "Chat");

            migrationBuilder.AddForeignKey(
                name: "FK_Chat_Department_DepartmentId",
                table: "Chat",
                column: "DepartmentId",
                principalTable: "Department",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }
    }
}
