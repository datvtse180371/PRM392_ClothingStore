using System;
using System.Collections.Generic;

namespace BLL.DTOs;

public class OrderDTO
{
    public int Id { get; set; }
    public int? UserId { get; set; }
    public DateTime? OrderDate { get; set; }
    public decimal TotalAmount { get; set; }
    public string Status { get; set; } = null!;
    public virtual ICollection<OrderItemDTO> OrderItems { get; set; } = new List<OrderItemDTO>();
    public virtual ICollection<PaymentDTO> Payments { get; set; } = new List<PaymentDTO>();
    public virtual UserDTO? User { get; set; }
}