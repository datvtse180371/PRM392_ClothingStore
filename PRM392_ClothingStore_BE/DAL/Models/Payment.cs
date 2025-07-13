using System;
using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace DAL.Models;

public partial class Payment
{
    public int Id { get; set; }

    public int? OrderId { get; set; }

    public string PaymentMethod { get; set; } = null!;

    [JsonPropertyName("paymentDate")]
    public DateTime? PaymentDate { get; set; }

    public string Status { get; set; } = null!;

    public virtual Order? Order { get; set; }
}
