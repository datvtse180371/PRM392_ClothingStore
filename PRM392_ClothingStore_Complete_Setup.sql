-- Complete Setup Script for PRM392 Clothing Store
-- This script creates the database, tables, and inserts sample data

/*
=== LOGIN CREDENTIALS FOR TESTING ===

Regular Users (Role: User):
- Email: john.doe@email.com       | Password: password123
- Email: jane.smith@email.com     | Password: password123
- Email: mike.johnson@email.com   | Password: password123
- Email: sarah.wilson@email.com   | Password: password123
- Email: test@email.com           | Password: test123

Admin User (Role: Admin):
- Email: admin@clothingstore.com  | Password: admin123

Note: All passwords are properly hashed with BCrypt for security.
*/

-- Create database if it doesn't exist
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'PRM392_ClothingStore_DB')
BEGIN
    CREATE DATABASE PRM392_ClothingStore_DB;
END
GO

USE PRM392_ClothingStore_DB;
GO

-- Drop tables if they exist (in correct order to avoid foreign key constraints)
IF OBJECT_ID('Payment', 'U') IS NOT NULL DROP TABLE Payment;
IF OBJECT_ID('OrderItem', 'U') IS NOT NULL DROP TABLE OrderItem;
IF OBJECT_ID('[Order]', 'U') IS NOT NULL DROP TABLE [Order];
IF OBJECT_ID('CartItem', 'U') IS NOT NULL DROP TABLE CartItem;
IF OBJECT_ID('Product', 'U') IS NOT NULL DROP TABLE Product;
IF OBJECT_ID('Category', 'U') IS NOT NULL DROP TABLE Category;
IF OBJECT_ID('[User]', 'U') IS NOT NULL DROP TABLE [User];

-- Create tables
CREATE TABLE [User] (
    Id INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100) NOT NULL,
    Email NVARCHAR(100) UNIQUE NOT NULL,
    Password NVARCHAR(100) NOT NULL,
    Role NVARCHAR(50) NOT NULL DEFAULT 'User'
);

CREATE TABLE Category (
    Id INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100) NOT NULL,
    Description NVARCHAR(500)
);

CREATE TABLE Product (
    Id INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(200) NOT NULL,
    Description NVARCHAR(1000),
    Price DECIMAL(10, 2) NOT NULL,
    Category INT FOREIGN KEY REFERENCES Category(Id),
    Size NVARCHAR(20),
    Color NVARCHAR(50),
    ImageUrl NVARCHAR(500),
    Stock INT NOT NULL DEFAULT 0,
    CHECK (Price >= 0),
    CHECK (Stock >= 0)
);

CREATE TABLE CartItem (
    Id INT PRIMARY KEY IDENTITY(1,1),
    UserId INT FOREIGN KEY REFERENCES [User](Id),
    AddedAt DATETIME DEFAULT GETDATE(),
    Quantity INT NOT NULL,
    ProductId INT FOREIGN KEY REFERENCES Product(Id),
    CHECK (Quantity > 0)
);

CREATE TABLE [Order] (
    Id INT PRIMARY KEY IDENTITY(1,1),
    UserId INT FOREIGN KEY REFERENCES [User](Id),
    OrderDate DATETIME DEFAULT GETDATE(),
    TotalAmount DECIMAL(10, 2) NOT NULL,
    Status NVARCHAR(50) NOT NULL,
    CHECK (TotalAmount >= 0)
);

CREATE TABLE OrderItem (
    Id INT PRIMARY KEY IDENTITY(1,1),
    OrderId INT FOREIGN KEY REFERENCES [Order](Id),
    ProductId INT FOREIGN KEY REFERENCES Product(Id),
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10, 2) NOT NULL,
    SubTotal AS (Quantity * UnitPrice) PERSISTED,
    CHECK (Quantity > 0)
);

CREATE TABLE Payment (
    Id INT PRIMARY KEY IDENTITY(1,1),
    OrderId INT FOREIGN KEY REFERENCES [Order](Id),
    PaymentMethod NVARCHAR(50) NOT NULL,
    PaymentDate DATETIME DEFAULT GETDATE(),
    Status NVARCHAR(50) NOT NULL
);

-- Insert Categories
INSERT INTO Category (Name, Description) VALUES
('T-Shirts', 'Comfortable and stylish t-shirts for everyday wear'),
('Jeans', 'Premium denim jeans in various styles and fits'),
('Dresses', 'Elegant dresses for all occasions'),
('Jackets', 'Stylish jackets and outerwear'),
('Shoes', 'Comfortable and fashionable footwear'),
('Accessories', 'Fashion accessories to complete your look'),
('Hoodies', 'Cozy hoodies and sweatshirts'),
('Shorts', 'Comfortable shorts for casual wear');

-- Insert Users with BCrypt hashed passwords
-- All passwords are hashed versions of simple passwords for testing
--Email: john.doe@email.com | Password: password123
--Email: jane.smith@email.com | Password: password123
--Email: mike.johnson@email.com | Password: password123
--Email: sarah.wilson@email.com | Password: password123
--Email: test@email.com | Password: test123
INSERT INTO [User] (Name, Email, Password, Role) VALUES
('John Doe', 'john.doe@email.com', '$2a$11$8K1p/a0dL2LkqvMA/ZXPseXEACfANdqfBdE9WfaGnVCf6fvzuBWrG', 'User'),        -- password: password123
('Jane Smith', 'jane.smith@email.com', '$2a$11$8K1p/a0dL2LkqvMA/ZXPseXEACfANdqfBdE9WfaGnVCf6fvzuBWrG', 'User'),      -- password: password123
('Mike Johnson', 'mike.johnson@email.com', '$2a$11$8K1p/a0dL2LkqvMA/ZXPseXEACfANdqfBdE9WfaGnVCf6fvzuBWrG', 'User'),    -- password: password123
('Sarah Wilson', 'sarah.wilson@email.com', '$2a$11$8K1p/a0dL2LkqvMA/ZXPseXEACfANdqfBdE9WfaGnVCf6fvzuBWrG', 'User'),    -- password: password123
('Admin User', 'admin@clothingstore.com', '$2a$11$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin'),     -- password: admin123
('Test User', 'test@email.com', '$2a$11$N9qo8uLOickgx2ZMRZoMye7VFnjZcHzTJeMjReVy0OaFLeiPuO6Zu', 'User');        -- password: test123

-- Insert Products - T-Shirts
INSERT INTO Product (Name, Description, Price, Category, Size, Color, ImageUrl, Stock) VALUES
('Classic White T-Shirt', 'Premium cotton white t-shirt, perfect for everyday wear', 19.99, 1, 'M', 'White', 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400', 50),
('Vintage Black Tee', 'Soft vintage-style black t-shirt with comfortable fit', 24.99, 1, 'L', 'Black', 'https://images.unsplash.com/photo-1583743814966-8936f37f4678?w=400', 35),
('Navy Blue Basic Tee', 'Essential navy blue t-shirt made from organic cotton', 22.99, 1, 'S', 'Navy', 'https://images.unsplash.com/photo-1586790170083-2f9ceadc732d?w=400', 40),
('Striped Cotton Tee', 'Classic striped t-shirt in blue and white', 26.99, 1, 'M', 'Blue/White', 'https://images.unsplash.com/photo-1562157873-818bc0726f68?w=400', 25),
('Red Graphic Tee', 'Bold red t-shirt with modern graphic design', 29.99, 1, 'L', 'Red', 'https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=400', 30);

-- Insert Products - Jeans
INSERT INTO Product (Name, Description, Price, Category, Size, Color, ImageUrl, Stock) VALUES
('Slim Fit Dark Jeans', 'Modern slim fit jeans in dark wash denim', 79.99, 2, '32', 'Dark Blue', 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=400', 20),
('Classic Straight Jeans', 'Timeless straight leg jeans in medium wash', 69.99, 2, '34', 'Medium Blue', 'https://images.unsplash.com/photo-1475178626620-a4d074967452?w=400', 25),
('Ripped Skinny Jeans', 'Trendy skinny jeans with stylish rips', 89.99, 2, '30', 'Light Blue', 'https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=400', 15),
('High-Waist Mom Jeans', 'Vintage-inspired high-waist jeans', 74.99, 2, '28', 'Medium Blue', 'https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=400', 18),
('Black Skinny Jeans', 'Sleek black skinny jeans for versatile styling', 84.99, 2, '32', 'Black', 'https://images.unsplash.com/photo-1506629905607-d405d7d3b0d2?w=400', 22);

-- Insert Products - Dresses
INSERT INTO Product (Name, Description, Price, Category, Size, Color, ImageUrl, Stock) VALUES
('Floral Summer Dress', 'Light and airy floral dress perfect for summer', 59.99, 3, 'M', 'Floral', 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=400', 12),
('Little Black Dress', 'Elegant black dress suitable for any occasion', 89.99, 3, 'S', 'Black', 'https://images.unsplash.com/photo-1566479179817-c0b8b8b5b8b8?w=400', 8),
('Casual Midi Dress', 'Comfortable midi dress for everyday wear', 49.99, 3, 'L', 'Navy', 'https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=400', 15),
('Bohemian Maxi Dress', 'Flowing bohemian-style maxi dress', 79.99, 3, 'M', 'Earth Tones', 'https://images.unsplash.com/photo-1515372039744-b8f02a3ae446?w=400', 10);

-- Insert Products - Jackets
INSERT INTO Product (Name, Description, Price, Category, Size, Color, ImageUrl, Stock) VALUES
('Denim Jacket', 'Classic denim jacket with vintage wash', 89.99, 4, 'M', 'Blue', 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=400', 16),
('Leather Biker Jacket', 'Premium leather jacket with modern cut', 199.99, 4, 'L', 'Black', 'https://images.unsplash.com/photo-1520975954732-35dd22299614?w=400', 8),
('Bomber Jacket', 'Trendy bomber jacket in olive green', 79.99, 4, 'M', 'Olive', 'https://images.unsplash.com/photo-1544966503-7cc5ac882d5f?w=400', 12),
('Wool Peacoat', 'Elegant wool peacoat for winter', 149.99, 4, 'L', 'Navy', 'https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=400', 6);

-- Insert Products - Shoes
INSERT INTO Product (Name, Description, Price, Category, Size, Color, ImageUrl, Stock) VALUES
('White Sneakers', 'Clean white sneakers for casual wear', 89.99, 5, '9', 'White', 'https://images.unsplash.com/photo-1549298916-b41d501d3772?w=400', 25),
('Black Boots', 'Stylish black ankle boots', 129.99, 5, '8', 'Black', 'https://images.unsplash.com/photo-1544966503-7cc5ac882d5f?w=400', 15),
('Canvas Sneakers', 'Comfortable canvas sneakers in navy', 59.99, 5, '10', 'Navy', 'https://images.unsplash.com/photo-1560769629-975ec94e6a86?w=400', 20),
('High Heels', 'Elegant black high heels for special occasions', 99.99, 5, '7', 'Black', 'https://images.unsplash.com/photo-1543163521-1bf539c55dd2?w=400', 10);

-- Insert Products - Hoodies
INSERT INTO Product (Name, Description, Price, Category, Size, Color, ImageUrl, Stock) VALUES
('Gray Pullover Hoodie', 'Cozy gray hoodie with kangaroo pocket', 49.99, 7, 'L', 'Gray', 'https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=400', 30),
('Black Zip Hoodie', 'Full-zip black hoodie with drawstring hood', 54.99, 7, 'M', 'Black', 'https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=400', 25),
('Navy Oversized Hoodie', 'Trendy oversized hoodie in navy blue', 59.99, 7, 'XL', 'Navy', 'https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=400', 20);

-- Insert Products - Accessories
INSERT INTO Product (Name, Description, Price, Category, Size, Color, ImageUrl, Stock) VALUES
('Leather Belt', 'Premium leather belt with silver buckle', 39.99, 6, 'One Size', 'Brown', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400', 40),
('Baseball Cap', 'Classic baseball cap with adjustable strap', 24.99, 6, 'One Size', 'Black', 'https://images.unsplash.com/photo-1588850561407-ed78c282e89b?w=400', 35),
('Silk Scarf', 'Elegant silk scarf with floral pattern', 34.99, 6, 'One Size', 'Multi', 'https://images.unsplash.com/photo-1601924994987-69e26d50dc26?w=400', 15),
('Sunglasses', 'Stylish aviator sunglasses with UV protection', 79.99, 6, 'One Size', 'Black', 'https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=400', 25);

-- Insert Sample Cart Items
INSERT INTO CartItem (UserId, ProductId, Quantity, AddedAt) VALUES
(1, 1, 2, GETDATE()),
(1, 5, 1, GETDATE()),
(1, 10, 1, GETDATE()),
(2, 3, 1, GETDATE()),
(2, 15, 1, GETDATE());

-- Insert Sample Orders
INSERT INTO [Order] (UserId, OrderDate, TotalAmount, Status) VALUES
(1, DATEADD(day, -7, GETDATE()), 149.97, 'Completed'),
(1, DATEADD(day, -3, GETDATE()), 89.99, 'Completed'),
(2, DATEADD(day, -5, GETDATE()), 104.98, 'Completed'),
(2, DATEADD(day, -1, GETDATE()), 59.99, 'Pending');

-- Insert Order Items
INSERT INTO OrderItem (OrderId, ProductId, Quantity, UnitPrice) VALUES
(1, 1, 2, 19.99),  -- Classic White T-Shirt x2
(1, 5, 1, 29.99),  -- Red Graphic Tee x1
(1, 6, 1, 79.99),  -- Slim Fit Dark Jeans x1
(2, 19, 1, 89.99), -- Denim Jacket x1
(3, 2, 1, 24.99),  -- Vintage Black Tee x1
(3, 6, 1, 79.99),  -- Slim Fit Dark Jeans x1
(4, 15, 1, 59.99); -- Floral Summer Dress x1

-- Insert Sample Payments
INSERT INTO Payment (OrderId, PaymentMethod, PaymentDate, Status) VALUES
(1, 'Credit Card', DATEADD(day, -7, GETDATE()), 'Completed'),
(2, 'PayPal', DATEADD(day, -3, GETDATE()), 'Completed'),
(3, 'Credit Card', DATEADD(day, -5, GETDATE()), 'Completed'),
(4, 'Credit Card', DATEADD(day, -1, GETDATE()), 'Pending');

-- Display summary
SELECT 'Database setup completed successfully!' as Message;
SELECT COUNT(*) as 'Total Categories' FROM Category;
SELECT COUNT(*) as 'Total Users' FROM [User];
SELECT COUNT(*) as 'Total Products' FROM Product;
SELECT COUNT(*) as 'Total Cart Items' FROM CartItem;
SELECT COUNT(*) as 'Total Orders' FROM [Order];
SELECT COUNT(*) as 'Total Order Items' FROM OrderItem;
SELECT COUNT(*) as 'Total Payments' FROM Payment;
