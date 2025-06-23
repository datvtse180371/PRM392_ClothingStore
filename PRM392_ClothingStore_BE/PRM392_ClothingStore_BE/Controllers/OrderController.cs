using BLL.DTOs;
using BLL.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

[Route("api/[controller]")]
[ApiController]
public class OrderController : ControllerBase
{
    private readonly IOrderService _service;

    public OrderController(IOrderService service) => _service = service;

    [HttpGet("{id}")]
    public async Task<ActionResult<OrderDTO>> GetById(int id) =>
        Ok(await _service.GetOrderByIdAsync(id));

    [HttpGet]
    public async Task<ActionResult<IEnumerable<OrderDTO>>> GetAll() =>
        Ok(await _service.GetAllOrdersAsync());

    [HttpGet("user/{userId}")]
    public async Task<ActionResult<IEnumerable<OrderDTO>>> GetByUserId(int userId) =>
        Ok(await _service.GetOrdersByUserIdAsync(userId));

    [HttpPost]
    public async Task<IActionResult> Add([FromBody] OrderDTO dto)
    {
        await _service.AddOrderAsync(dto);
        return Ok();
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, [FromBody] OrderDTO dto)
    {
        if (id != dto.Id) return BadRequest();
        await _service.UpdateOrderAsync(dto);
        return NoContent();
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        await _service.DeleteOrderAsync(id);
        return NoContent();
    }
}
