using BLL.DTOs;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace BLL.Services.Interfaces
{
    public interface IUserService
    {
        Task<UserDTO> GetByIdAsync(int id);
        Task<IEnumerable<UserDTO>> GetAllAsync();
        Task AddAsync(UserDTO userDto);
        Task UpdateAsync(UserDTO userDto);
        Task DeleteAsync(int id);
        Task<(string Token, UserDTO User)> LoginAsync(LoginDTO loginDto);
    }
}