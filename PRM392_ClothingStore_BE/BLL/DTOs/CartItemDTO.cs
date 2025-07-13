using System;
using System.Text.Json.Serialization;

namespace BLL.DTOs;

public class CartItemDTO
{
    public int Id { get; set; }
    public int? UserId { get; set; }
    [JsonPropertyName("addedAt")]
    public DateTime? AddedAt { get; set; }
    public int Quantity { get; set; }
    public int? ProductId { get; set; }
}