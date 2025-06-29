﻿using System;

namespace BLL.DTOs;

public class OrderItemDTO
{
    public int Id { get; set; }
    public int? OrderId { get; set; }
    public int? ProductId { get; set; }
    public int Quantity { get; set; }
    public decimal UnitPrice { get; set; }
    public decimal? SubTotal { get; set; }
}