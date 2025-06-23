using BLL.DTOs;
using BLL.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

[Route("api/[controller]")]
[ApiController]
public class UserController : ControllerBase
{
    private readonly IUserService _service;

    public UserController(IUserService service) => _service = service;

    [HttpGet("{id}")]
    public async Task<ActionResult<UserDTO>> GetById(int id) =>
        Ok(await _service.GetUserByIdAsync(id));

    [HttpGet]
    public async Task<ActionResult<IEnumerable<UserDTO>>> GetAll() =>
        Ok(await _service.GetAllUsersAsync());

    [HttpPost]
    public async Task<IActionResult> Add([FromBody] UserDTO dto)
    {
        await _service.AddUserAsync(dto);
        return Ok();
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(int id, [FromBody] UserDTO dto)
    {
        if (id != dto.Id) return BadRequest();
        await _service.UpdateUserAsync(dto);
        return NoContent();
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {
        await _service.DeleteUserAsync(id);
        return NoContent();
    }
}
