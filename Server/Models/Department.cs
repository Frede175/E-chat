using Server.DbModels;

namespace Server.Models
{
    public class Department
    {

        public Department(DbModels.Department department)
        {
            Id = department.Id;
            Name = department.Name;
        }

        public int Id { get; set; }

        public string Name { get; set; }
    }
}