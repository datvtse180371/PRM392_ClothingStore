using System.Collections.Generic;

namespace BLL.DTOs;

public class CategoryDTO
{
    public int Id { get; set; }
    public string Name { get; set; } = null!;
    public string? Description { get; set; }
    public virtual ICollection<ProductDTO> Products { get; set; } = new List<ProductDTO>();
}