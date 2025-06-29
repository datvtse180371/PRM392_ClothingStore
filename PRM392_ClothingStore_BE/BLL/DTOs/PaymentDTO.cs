using System;

namespace BLL.DTOs;

public class PaymentDTO
{
    public int Id { get; set; }
    public int? OrderId { get; set; }
    public string PaymentMethod { get; set; } = null!;
    public DateTime? PaymentDate { get; set; }
    public string Status { get; set; } = null!;
}