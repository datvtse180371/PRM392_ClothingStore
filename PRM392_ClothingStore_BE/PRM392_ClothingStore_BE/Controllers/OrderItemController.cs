using BLL.DTOs;
using BLL.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

[Route("api/[controller]")]
[ApiController]
public class OrderItemController : ControllerBase
{
    private readonly IOrderItemService _service;

    public OrderItemController(IOrderItemService service) => _service = service;

    [HttpGet("{id}")]
    public async Task<ActionResult<OrderItemDTO>> GetById(int id) =>
        Ok(await _service.GetOrderItemByIdAsync(id));

    [HttpGet]
    public async Task<ActionResult<IEnumerable<OrderItemDTO>>> GetAll() =>
        Ok(await _service.GetAllOrderItemsAsync());

    [HttpGet("order/{orderId}")]
    public async Task<ActionResult<IEnumerable<OrderItemDTO>>> GetByOrderId(int orderId) =>
        Ok(await _service.GetOrderItemsByOrderIdAsync(orderId));

    [HttpPost]
    public async Task<IActionResult> Add([FromBody] OrderItemDTO dto)
    {
        await _service.AddOrderItemAsync(dto);
        return Ok();
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, [FromBody] OrderItemDTO dto)
    {
        if (id != dto.Id) return BadRequest();
        await _service.UpdateOrderItemAsync(dto);
        return NoContent();
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        await _service.DeleteOrderItemAsync(id);
        return NoContent();
    }
}
