using System.Collections.Generic;

namespace BLL.DTOs;

public class ProductDTO
{
    public int Id { get; set; }
    public string Name { get; set; } = null!;
    public string? Description { get; set; }
    public decimal Price { get; set; }
    public int? Category { get; set; }
    public string? Size { get; set; }
    public string? Color { get; set; }
    public string? ImageUrl { get; set; }
    public int Stock { get; set; }
    public virtual CategoryDTO? CategoryNavigation { get; set; }
    // Removed circular references to prevent serialization issues
    // public virtual ICollection<CartItemDTO> CartItems { get; set; } = new List<CartItemDTO>();
    // public virtual ICollection<OrderItemDTO> OrderItems { get; set; } = new List<OrderItemDTO>();
}