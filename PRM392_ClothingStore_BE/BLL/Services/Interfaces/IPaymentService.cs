using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BLL.DTOs;

namespace BLL.Services.Interfaces
{
    public interface IPaymentService
    {
        Task<PaymentDTO> GetPaymentByIdAsync(int id);
        Task<IEnumerable<PaymentDTO>> GetAllPaymentsAsync();
        Task<IEnumerable<PaymentDTO>> GetPaymentsByUserIdAsync(int userId);
        Task AddPaymentAsync(PaymentDTO paymentDto);
        Task UpdatePaymentAsync(PaymentDTO paymentDto);
        Task DeletePaymentAsync(int id);
    }
}
