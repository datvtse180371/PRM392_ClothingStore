using BLL.DTOs;
using BLL.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

[Route("api/[controller]")]
[ApiController]
public class PaymentController : ControllerBase
{
    private readonly IPaymentService _service;

    public PaymentController(IPaymentService service) => _service = service;

    [HttpGet("{id}")]
    public async Task<ActionResult<PaymentDTO>> GetById(int id) =>
        Ok(await _service.GetPaymentByIdAsync(id));

    [HttpGet]
    public async Task<ActionResult<IEnumerable<PaymentDTO>>> GetAll() =>
        Ok(await _service.GetAllPaymentsAsync());

    [HttpGet("user/{userId}")]
    public async Task<ActionResult<IEnumerable<PaymentDTO>>> GetByUserId(int userId) =>
        Ok(await _service.GetPaymentsByUserIdAsync(userId));

    [HttpPost]
    public async Task<IActionResult> Add([FromBody] PaymentDTO dto)
    {
        await _service.AddPaymentAsync(dto);
        return Ok();
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, [FromBody] PaymentDTO dto)
    {
        if (id != dto.Id) return BadRequest();
        await _service.UpdatePaymentAsync(dto);
        return NoContent();
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        await _service.DeletePaymentAsync(id);
        return NoContent();
    }
}
