using System;

namespace BLL.DTOs;

public class CartItemDTO
{
    public int Id { get; set; }
    public int? UserId { get; set; }
    public DateTime? AddedAt { get; set; }
    public int Quantity { get; set; }
    public int? ProductId { get; set; }
}