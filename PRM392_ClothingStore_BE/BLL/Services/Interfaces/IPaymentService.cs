using BLL.DTOs;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace BLL.Services.Interfaces
{
    public interface IPaymentService
    {
        Task<PaymentDTO> GetByIdAsync(int id);
        Task<IEnumerable<PaymentDTO>> GetAllAsync();
        Task AddAsync(PaymentDTO paymentDto);
        Task UpdateAsync(PaymentDTO paymentDto);
        Task DeleteAsync(int id);
    }
}