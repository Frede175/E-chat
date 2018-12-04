using System.ComponentModel.DataAnnotations;

namespace Server.Models
{
    public class Page
    {
        [Required]
        [Range(0, int.MaxValue)]
        public int PageNumber {get; set;}

        [Required]
        [Range(10, int.MaxValue)]
        public int PageSize { get; set; }
        
    }
}