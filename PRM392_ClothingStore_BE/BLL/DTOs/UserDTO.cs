using System.Collections.Generic;

namespace BLL.DTOs;

public class UserDTO
{
    public int Id { get; set; }
    public string Name { get; set; } = null!;
    public string Email { get; set; } = null!;
    public string Password { get; set; } = null!;
    public string Role { get; set; } = null!;
    public virtual ICollection<CartItemDTO> CartItems { get; set; } = new List<CartItemDTO>();
    public virtual ICollection<OrderDTO> Orders { get; set; } = new List<OrderDTO>();
}