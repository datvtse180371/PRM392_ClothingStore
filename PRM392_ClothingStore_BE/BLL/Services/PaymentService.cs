using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BLL.DTOs;
using BLL.Services.Interfaces;

namespace BLL.Services
{
    public class PaymentService : IPaymentService
    {
        public Task AddPaymentAsync(PaymentDTO paymentDto)
        {
            throw new NotImplementedException();
        }

        public Task DeletePaymentAsync(int id)
        {
            throw new NotImplementedException();
        }

        public Task<IEnumerable<PaymentDTO>> GetAllPaymentsAsync()
        {
            throw new NotImplementedException();
        }

        public Task<PaymentDTO> GetPaymentByIdAsync(int id)
        {
            throw new NotImplementedException();
        }

        public Task<IEnumerable<PaymentDTO>> GetPaymentsByUserIdAsync(int userId)
        {
            throw new NotImplementedException();
        }

        public Task UpdatePaymentAsync(PaymentDTO paymentDto)
        {
            throw new NotImplementedException();
        }
    }
}
