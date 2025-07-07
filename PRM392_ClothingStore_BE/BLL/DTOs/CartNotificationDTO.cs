using System;
using System.Collections.Generic;

namespace BLL.DTOs;

public class CartNotificationDTO
{
    public bool HasItems { get; set; }
    public int TotalItems { get; set; }
    public int UniqueProducts { get; set; }
    public string Message { get; set; } = string.Empty;
    public List<CartItemWithProductDTO> CartItems { get; set; } = new List<CartItemWithProductDTO>();
}

public class CartItemWithProductDTO
{
    public int Id { get; set; }
    public int? UserId { get; set; }
    public DateTime? AddedAt { get; set; }
    public int Quantity { get; set; }
    public int? ProductId { get; set; }
    public ProductDTO? Product { get; set; }
}
