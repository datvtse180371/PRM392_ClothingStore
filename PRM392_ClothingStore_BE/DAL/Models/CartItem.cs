using System;
using System.Collections.Generic;

namespace DAL.Models;

public partial class CartItem
{
    public int Id { get; set; }

    public int? UserId { get; set; }

    public DateTime? AddedAt { get; set; }

    public int Quantity { get; set; }

    public int? ProductId { get; set; }

    public virtual Product? Product { get; set; }

    public virtual User? User { get; set; }
}
