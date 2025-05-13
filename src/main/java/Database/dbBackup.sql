-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 09, 2025 at 06:31 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pharmacyv1`
--

-- --------------------------------------------------------

--
-- Table structure for table `creditcard`
--

CREATE TABLE `creditcard` (
  `CardID` int(11) NOT NULL,
  `CardNumber` varchar(16) NOT NULL,
  `CardValidThru` date DEFAULT NULL,
  `CardholderName` varchar(255) DEFAULT NULL,
  `CardCVC` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `creditcard`
--

INSERT INTO `creditcard` (`CardID`, `CardNumber`, `CardValidThru`, `CardholderName`, `CardCVC`) VALUES
(1, '1234567890123456', '2012-01-12', '1231241213', 1234),
(2, '3213123213212130', '2030-05-01', 'Fahd', 3214),
(3, '3213123213212130', '2025-05-01', 'Fahd', 3214),
(4, '1323211321232130', '2026-10-01', 'Me', 1234),
(5, '2311231231231232', '2025-12-01', 'Fahd Ahmed', 123),
(6, '1231231231231233', '2033-12-01', 'Me', 1234);

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `CustomerID` int(11) NOT NULL,
  `CustomerName` varchar(100) DEFAULT NULL,
  `PharmacyAssociation` varchar(100) DEFAULT NULL,
  `CustomerAddress` varchar(255) DEFAULT NULL,
  `PersonalPhoneNumber` varchar(20) DEFAULT NULL,
  `HealthInsuranceNumber` varchar(20) DEFAULT NULL,
  `CustomerDebt` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`CustomerID`, `CustomerName`, `PharmacyAssociation`, `CustomerAddress`, `PersonalPhoneNumber`, `HealthInsuranceNumber`, `CustomerDebt`) VALUES
(1, 'Ahmed Ali', 'Pharmacy Association 1', '123 Main Street, Cairo', '0123456789', '1234567890', '0.00'),
(2, 'Fatima Mohamed', 'Pharmacy Association 3', '456 Elm Street, Giza', '0111111111', '0987654321', '200.00'),
(3, 'Hanady Abdallah', 'VIP Customer', 'New Cairo Behind New Cairo Hospital', '0100124525', '9810216515', '0.00'),
(4, 'Kiro Fawzy', 'Owner', 'Home', '21303', '2161', '0.00');

-- --------------------------------------------------------

--
-- Table structure for table `employees`
--

CREATE TABLE `employees` (
  `EmployeeID` int(11) NOT NULL,
  `EmployeeName` varchar(100) DEFAULT NULL,
  `EmployeePhone` varchar(20) DEFAULT NULL,
  `EmployeeAddress` varchar(255) DEFAULT NULL,
  `JobTitle` varchar(100) DEFAULT NULL,
  `DateOfBirth` date DEFAULT NULL,
  `EmploymentStartDate` date DEFAULT NULL,
  `Salary` decimal(10,2) DEFAULT NULL,
  `HealthInsuranceNumber` varchar(20) DEFAULT NULL,
  `EducationalDegree` varchar(100) DEFAULT NULL,
  `MilitaryStatus` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employees`
--

INSERT INTO `employees` (`EmployeeID`, `EmployeeName`, `EmployeePhone`, `EmployeeAddress`, `JobTitle`, `DateOfBirth`, `EmploymentStartDate`, `Salary`, `HealthInsuranceNumber`, `EducationalDegree`, `MilitaryStatus`) VALUES
(1, '1', '0123456789', '123 Maple Street, Luxor', 'Pharmacist', '1990-01-01', '2015-01-01', '5000.00', '1234567890', 'Pharmacy Degree', 'Completed'),
(2, 'Employee 2', '0111111111', '456 Cedar Street, Aswan', 'Cashier', '1995-05-05', '2018-01-01', '3000.00', '0987654321', 'High School Diploma', 'Exempted'),
(3, 'Fahd Ahmed', '01008643277', '456 Karor Street, Aswan', 'Manager', '2003-05-07', '2023-01-01', '20000.00', '3264575867', 'High School Diploma', 'Exempted'),
(4, 'Admin', '01001234567', 'Aswan , Best Pharmacy', 'Admin', '1980-02-22', '2000-02-22', '0.00', '1467859886', 'Pharmacy Degree', 'Completed');

-- --------------------------------------------------------

--
-- Table structure for table `medications`
--

CREATE TABLE `medications` (
  `MedicationID` int(11) NOT NULL,
  `MedicationBarcode` varchar(50) DEFAULT NULL,
  `ArabicName` varchar(100) DEFAULT NULL,
  `EnglishName` varchar(100) DEFAULT NULL,
  `InternationalCode` varchar(50) DEFAULT NULL,
  `ActiveIngredient` varchar(100) DEFAULT NULL,
  `Manufacturer` varchar(100) DEFAULT NULL,
  `ExpiryDate` date DEFAULT NULL,
  `Unit` varchar(50) DEFAULT NULL,
  `Quantity` int(11) DEFAULT NULL,
  `SellingPrice` decimal(10,2) DEFAULT NULL,
  `PurchasePrice` decimal(10,2) DEFAULT NULL,
  `ReorderLevel` int(11) DEFAULT NULL,
  `MedicationType` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `medications`
--

INSERT INTO `medications` (`MedicationID`, `MedicationBarcode`, `ArabicName`, `EnglishName`, `InternationalCode`, `ActiveIngredient`, `Manufacturer`, `ExpiryDate`, `Unit`, `Quantity`, `SellingPrice`, `PurchasePrice`, `ReorderLevel`, `MedicationType`) VALUES
(1, '123123', 'بينادول', 'Penadol', '960430', 'Penadol', 'Penadol.inc', '2012-06-01', 'Box', 1000, '40.00', '39.00', 500, 'Capsoule'),
(2, '9413', 'دواء كحة', 'Cough medicine', '321', 'syrem', 'ABC', '2026-05-15', 'شريط', 2000, '90.00', '90.00', 500, 'شراب'),
(3, '123654', 'دواء للبرد', 'Cold Medicine', '9027', 'Coal', 'Company B', '2028-05-29', 'ml', 500, '35.00', '30.00', 9000, 'Drink'),
(4, '111', 'دواء 69', 'Medication 69', '159', 'Alpha', 'Alpha', '2027-06-22', '2', 0, '9.00', '8.00', 10, 'شرب');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `ProductID` int(11) NOT NULL,
  `ProductBarcode` varchar(50) DEFAULT NULL,
  `ArabicName` varchar(100) DEFAULT NULL,
  `EnglishName` varchar(100) DEFAULT NULL,
  `SellingPrice` decimal(10,2) DEFAULT NULL,
  `PurchasePrice` decimal(10,2) DEFAULT NULL,
  `Quantity` int(11) DEFAULT NULL,
  `Manufacturer` varchar(100) DEFAULT NULL,
  `ProductType` varchar(100) DEFAULT NULL,
  `ExpiryDate` date DEFAULT NULL,
  `ReorderLevel` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`ProductID`, `ProductBarcode`, `ArabicName`, `EnglishName`, `SellingPrice`, `PurchasePrice`, `Quantity`, `Manufacturer`, `ProductType`, `ExpiryDate`, `ReorderLevel`) VALUES
(1, '654321', 'مستحضر 1', 'Product 1', '15.00', '10.00', 200, 'Manufacturer 1', 'Type A', '2025-01-01', 10),
(2, '000000', 'مستحضر 2', 'Product 2', '20.00', '10.00', 700, 'Manufacturer 2', 'Type B', '2025-02-01', 15),
(3, '312', 'مرهم خارجي', 'Cream', '50.00', '12.00', 500, 'Smooth Skin', 'External', '2019-05-15', 1000);

-- --------------------------------------------------------

--
-- Table structure for table `purchaseinvoicedetails`
--

CREATE TABLE `purchaseinvoicedetails` (
  `PurchaseInvoiceDetailID` int(11) NOT NULL,
  `PurchaseInvoiceID` int(11) DEFAULT NULL,
  `ProductType` varchar(50) DEFAULT NULL,
  `Quantity` int(11) DEFAULT NULL,
  `ExpiryDate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `purchaseinvoicedetails`
--

INSERT INTO `purchaseinvoicedetails` (`PurchaseInvoiceDetailID`, `PurchaseInvoiceID`, `ProductType`, `Quantity`, `ExpiryDate`) VALUES
(1, 1, 'Medication', 50, '2024-06-01'),
(2, 2, 'Product', 30, '2025-01-01');

-- --------------------------------------------------------

--
-- Table structure for table `purchaseinvoices`
--

CREATE TABLE `purchaseinvoices` (
  `PurchaseInvoiceID` int(11) NOT NULL,
  `PurchaseDate` date DEFAULT NULL,
  `SupplierID` int(11) DEFAULT NULL,
  `TotalCost` decimal(10,2) DEFAULT NULL,
  `InvoiceStatus` varchar(265) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `purchaseinvoices`
--

INSERT INTO `purchaseinvoices` (`PurchaseInvoiceID`, `PurchaseDate`, `SupplierID`, `TotalCost`, `InvoiceStatus`) VALUES
(1, '2024-05-01', 1, '150.00', 'Done'),
(2, '2024-05-02', 2, '200.00', 'On hold');

-- --------------------------------------------------------

--
-- Table structure for table `purchasereturndetails`
--

CREATE TABLE `purchasereturndetails` (
  `PurchaseReturnDetailID` int(11) NOT NULL,
  `PurchaseReturnID` int(11) DEFAULT NULL,
  `ProductType` varchar(50) DEFAULT NULL,
  `QuantityReturned` int(11) DEFAULT NULL,
  `RefundedAmount` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `purchasereturndetails`
--

INSERT INTO `purchasereturndetails` (`PurchaseReturnDetailID`, `PurchaseReturnID`, `ProductType`, `QuantityReturned`, `RefundedAmount`) VALUES
(1, 1, 'Medication', 10, '15.00'),
(2, 2, 'Product', 8, '20.00');

-- --------------------------------------------------------

--
-- Table structure for table `purchasereturns`
--

CREATE TABLE `purchasereturns` (
  `PurchaseReturnID` int(11) NOT NULL,
  `SupplierID` int(11) DEFAULT NULL,
  `PurchaseInvoiceID` int(11) DEFAULT NULL,
  `ReturnDate` date DEFAULT NULL,
  `TotalReturnAmount` decimal(10,2) DEFAULT NULL,
  `Notes` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `purchasereturns`
--

INSERT INTO `purchasereturns` (`PurchaseReturnID`, `SupplierID`, `PurchaseInvoiceID`, `ReturnDate`, `TotalReturnAmount`, `Notes`) VALUES
(1, 1, 1, '2024-05-07', '30.00', 'Overstocked items returned by supplier'),
(2, 2, 2, '2024-05-08', '40.00', 'Supplier sent wrong products');

-- --------------------------------------------------------

--
-- Table structure for table `salesinvoicedetails`
--

CREATE TABLE `salesinvoicedetails` (
  `SalesInvoiceDetailID` int(11) NOT NULL,
  `SalesInvoiceID` int(11) DEFAULT NULL,
  `ProductType` varchar(50) DEFAULT NULL,
  `Quantity` int(11) DEFAULT NULL,
  `ItemID` int(11) DEFAULT NULL,
  `SellingPrice` double DEFAULT NULL,
  `Unit` varchar(255) DEFAULT NULL,
  `ItemName` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `salesinvoicedetails`
--

INSERT INTO `salesinvoicedetails` (`SalesInvoiceDetailID`, `SalesInvoiceID`, `ProductType`, `Quantity`, `ItemID`, `SellingPrice`, `Unit`, `ItemName`) VALUES
(1, 1, 'Medication', 20, 1, 100, 'Tablet', 'Penadol'),
(2, 2, 'Product', 15, 2, 50, 'box', 'Cough'),
(63, 43, 'Capsoule', 99, 1, 40, 'Box', 'Penadol'),
(64, -1, 'Capsoule', 1, 1, 40, 'Box', 'Penadol'),
(65, -1, 'Type A', 10, 1, 15, 'Box', 'Product 1'),
(66, 45, 'Type A', 10, 1, 15, 'Box', 'Product 1'),
(67, 45, 'Capsoule', 10, 1, 40, 'Box', 'Penadol'),
(68, 46, 'شراب', 1, 2, 90, 'شريط', 'Cough medicine'),
(69, 47, 'External', 2, 3, 50, 'ml', 'Cream'),
(70, 48, 'External', 2, 3, 50, 'ml', 'Cream'),
(71, 49, 'External', 10, 3, 50, 'ml', 'Cream'),
(72, 50, 'شرب', 9, 4, 9, 'شريط', 'Medication 69');

-- --------------------------------------------------------

--
-- Table structure for table `salesinvoices`
--

CREATE TABLE `salesinvoices` (
  `SalesInvoiceID` int(11) NOT NULL,
  `CustomerID` int(11) DEFAULT NULL,
  `TotalSaleAmount` decimal(10,2) DEFAULT NULL,
  `SaleStatus` varchar(50) DEFAULT NULL,
  `SalesDate` date DEFAULT NULL,
  `DiscountAmount` int(11) DEFAULT NULL,
  `TotalProfit` double DEFAULT NULL,
  `notes` varchar(255) NOT NULL,
  `PaymentMethod` varchar(255) DEFAULT NULL,
  `CashairName` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `salesinvoices`
--

INSERT INTO `salesinvoices` (`SalesInvoiceID`, `CustomerID`, `TotalSaleAmount`, `SaleStatus`, `SalesDate`, `DiscountAmount`, `TotalProfit`, `notes`, `PaymentMethod`, `CashairName`) VALUES
(1, 1, '300.00', 'Paid', '2024-05-03', 10, 5, 'No Notes', 'Cash', '1'),
(2, 2, '250.00', 'Hold', '2024-05-04', 20, 2, 'Arrive as soon as possible', 'Visa', 'Employee 2'),
(43, 1, '3960.00', 'Complete', '2024-05-22', 0, 99, 'No Notes', 'Cash', '1'),
(45, 1, '495.00', 'Complete', '2024-05-23', 55, 60, 'No Notes', 'نقد', 'Admin'),
(46, 1, '633.60', 'Complete', '2024-05-23', 6, 60, 'No Notes', 'في الانتظار', 'Admin'),
(47, 1, '0.00', 'Complete', '2024-06-17', 100, 76, 'سبب الخصم امي', 'توصيل للمنزل', '1'),
(48, 2, '0.00', 'Complete', '2024-06-17', 100, 76, 'my mom', 'نقد', '1'),
(49, 3, '0.00', 'Complete', '2024-06-17', 500, 380, 'Because She Is My Mom', 'توصيل للمنزل', '1'),
(50, 4, '0.00', 'Complete', '2024-06-29', 81, 9, 'Ma bro', 'توصيل للمنزل', 'Admin');

-- --------------------------------------------------------

--
-- Table structure for table `salesreturndetails`
--

CREATE TABLE `salesreturndetails` (
  `SalesReturnDetailID` int(11) NOT NULL,
  `SalesReturnID` int(11) DEFAULT NULL,
  `SalesInvoiceDetailID` int(11) DEFAULT NULL,
  `ReturnDate` date DEFAULT NULL,
  `RefundedAmount` decimal(10,2) DEFAULT NULL,
  `QuantityReturned` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `salesreturndetails`
--

INSERT INTO `salesreturndetails` (`SalesReturnDetailID`, `SalesReturnID`, `SalesInvoiceDetailID`, `ReturnDate`, `RefundedAmount`, `QuantityReturned`) VALUES
(1, 1, 1, '2024-05-05', '10.00', 5),
(2, 2, 2, '2024-05-06', '15.00', 3);

-- --------------------------------------------------------

--
-- Table structure for table `salesreturns`
--

CREATE TABLE `salesreturns` (
  `SalesReturnID` int(11) NOT NULL,
  `CustomerID` int(11) DEFAULT NULL,
  `SalesInvoiceID` int(11) DEFAULT NULL,
  `ReturnDate` date DEFAULT NULL,
  `TotalReturnAmount` decimal(10,2) DEFAULT NULL,
  `Notes` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `salesreturns`
--

INSERT INTO `salesreturns` (`SalesReturnID`, `CustomerID`, `SalesInvoiceID`, `ReturnDate`, `TotalReturnAmount`, `Notes`) VALUES
(1, 1, 1, '2024-05-05', '50.00', 'Defective products returned by customer'),
(2, 2, 2, '2024-05-06', '30.00', 'Customer changed their mind');

-- --------------------------------------------------------

--
-- Table structure for table `stock`
--

CREATE TABLE `stock` (
  `StockID` int(11) NOT NULL,
  `StockType` varchar(50) DEFAULT NULL,
  `Location` varchar(100) DEFAULT NULL,
  `ReorderPoint` int(11) DEFAULT NULL,
  `QuantityInStock` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stock`
--

INSERT INTO `stock` (`StockID`, `StockType`, `Location`, `ReorderPoint`, `QuantityInStock`) VALUES
(1, 'Medications', 'Shelf A', 2, 100),
(2, 'Products', 'Shelf B', 100, 80);

-- --------------------------------------------------------

--
-- Table structure for table `suppliers`
--

CREATE TABLE `suppliers` (
  `SupplierID` int(11) NOT NULL,
  `SupplierName` varchar(100) DEFAULT NULL,
  `SupplierPhone` varchar(20) DEFAULT NULL,
  `SupplierAddress` varchar(255) DEFAULT NULL,
  `CurrentBalance` decimal(10,2) DEFAULT NULL,
  `MaximumLimit` decimal(10,2) DEFAULT NULL,
  `SupplierEmail` varchar(100) DEFAULT NULL,
  `ParentCompany` varchar(100) DEFAULT NULL,
  `ReturnsPolicy` varchar(256) DEFAULT NULL,
  `Notes` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `suppliers`
--

INSERT INTO `suppliers` (`SupplierID`, `SupplierName`, `SupplierPhone`, `SupplierAddress`, `CurrentBalance`, `MaximumLimit`, `SupplierEmail`, `ParentCompany`, `ReturnsPolicy`, `Notes`) VALUES
(1, 'Supplier 1', '0123456789', '789 Oak Street, Alexandria', '7000.00', '10000.00', 'supplier1@example.com', 'Company A', 'Must have the Invoice and gives half the money you have', 'Bald Guy'),
(2, 'Supplier 2', '0111111111', '321 Pine Street, Sharm El Sheikh', '6000.00', '8000.00', 'supplier2@example.com', 'Company B', 'Mustn\'t have the Invoice\nMust deal with the maneger', 'Slim Woman'),
(3, 'ABC Company', '123-456-7890', '123 Main Street, City, Country', '1000.00', '5000.00', 'abc@example.com', 'ABC', '30 days return policy', 'New supplier, reliable service.'),
(5, 'DEF Enterprises', '555-123-4567', '789 Elm Street, City, Country', '2000.00', '10000.00', 'def@example.com', 'GHI Group', '14 days return policy', 'Emerging supplier, potential for growth.');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `creditcard`
--
ALTER TABLE `creditcard`
  ADD PRIMARY KEY (`CardID`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`CustomerID`);

--
-- Indexes for table `employees`
--
ALTER TABLE `employees`
  ADD PRIMARY KEY (`EmployeeID`);

--
-- Indexes for table `medications`
--
ALTER TABLE `medications`
  ADD PRIMARY KEY (`MedicationID`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`ProductID`);

--
-- Indexes for table `purchaseinvoicedetails`
--
ALTER TABLE `purchaseinvoicedetails`
  ADD PRIMARY KEY (`PurchaseInvoiceDetailID`),
  ADD KEY `PurchaseInvoiceID` (`PurchaseInvoiceID`);

--
-- Indexes for table `purchaseinvoices`
--
ALTER TABLE `purchaseinvoices`
  ADD PRIMARY KEY (`PurchaseInvoiceID`),
  ADD KEY `SupplierID` (`SupplierID`);

--
-- Indexes for table `purchasereturndetails`
--
ALTER TABLE `purchasereturndetails`
  ADD PRIMARY KEY (`PurchaseReturnDetailID`),
  ADD KEY `PurchaseReturnID` (`PurchaseReturnID`);

--
-- Indexes for table `purchasereturns`
--
ALTER TABLE `purchasereturns`
  ADD PRIMARY KEY (`PurchaseReturnID`),
  ADD KEY `PurchaseInvoiceID` (`PurchaseInvoiceID`),
  ADD KEY `SupplierID` (`SupplierID`);

--
-- Indexes for table `salesinvoicedetails`
--
ALTER TABLE `salesinvoicedetails`
  ADD PRIMARY KEY (`SalesInvoiceDetailID`),
  ADD KEY `SalesInvoiceID` (`SalesInvoiceID`),
  ADD KEY `salesinvoicedetails_medications_MedicationID_fk` (`ItemID`);

--
-- Indexes for table `salesinvoices`
--
ALTER TABLE `salesinvoices`
  ADD PRIMARY KEY (`SalesInvoiceID`),
  ADD KEY `CustomerID` (`CustomerID`);

--
-- Indexes for table `salesreturndetails`
--
ALTER TABLE `salesreturndetails`
  ADD PRIMARY KEY (`SalesReturnDetailID`),
  ADD KEY `SalesInvoiceDetailID` (`SalesInvoiceDetailID`),
  ADD KEY `SalesReturnID` (`SalesReturnID`);

--
-- Indexes for table `salesreturns`
--
ALTER TABLE `salesreturns`
  ADD PRIMARY KEY (`SalesReturnID`),
  ADD KEY `CustomerID` (`CustomerID`),
  ADD KEY `SalesInvoiceID` (`SalesInvoiceID`);

--
-- Indexes for table `stock`
--
ALTER TABLE `stock`
  ADD PRIMARY KEY (`StockID`);

--
-- Indexes for table `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`SupplierID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `creditcard`
--
ALTER TABLE `creditcard`
  MODIFY `CardID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `salesinvoicedetails`
--
ALTER TABLE `salesinvoicedetails`
  MODIFY `SalesInvoiceDetailID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=73;

--
-- AUTO_INCREMENT for table `salesinvoices`
--
ALTER TABLE `salesinvoices`
  MODIFY `SalesInvoiceID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `purchaseinvoicedetails`
--
ALTER TABLE `purchaseinvoicedetails`
  ADD CONSTRAINT `purchaseinvoicedetails_ibfk_1` FOREIGN KEY (`PurchaseInvoiceID`) REFERENCES `purchaseinvoices` (`PurchaseInvoiceID`);

--
-- Constraints for table `purchaseinvoices`
--
ALTER TABLE `purchaseinvoices`
  ADD CONSTRAINT `purchaseinvoices_ibfk_1` FOREIGN KEY (`SupplierID`) REFERENCES `suppliers` (`SupplierID`);

--
-- Constraints for table `purchasereturndetails`
--
ALTER TABLE `purchasereturndetails`
  ADD CONSTRAINT `purchasereturndetails_ibfk_1` FOREIGN KEY (`PurchaseReturnID`) REFERENCES `purchasereturns` (`PurchaseReturnID`);

--
-- Constraints for table `purchasereturns`
--
ALTER TABLE `purchasereturns`
  ADD CONSTRAINT `purchasereturns_ibfk_1` FOREIGN KEY (`SupplierID`) REFERENCES `suppliers` (`SupplierID`),
  ADD CONSTRAINT `purchasereturns_ibfk_2` FOREIGN KEY (`PurchaseInvoiceID`) REFERENCES `purchaseinvoices` (`PurchaseInvoiceID`);

--
-- Constraints for table `salesinvoices`
--
ALTER TABLE `salesinvoices`
  ADD CONSTRAINT `salesinvoices_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `customers` (`CustomerID`);

--
-- Constraints for table `salesreturndetails`
--
ALTER TABLE `salesreturndetails`
  ADD CONSTRAINT `salesreturndetails_ibfk_1` FOREIGN KEY (`SalesReturnID`) REFERENCES `salesreturns` (`SalesReturnID`),
  ADD CONSTRAINT `salesreturndetails_salesinvoicedetails_SalesInvoiceDetailID_fk` FOREIGN KEY (`SalesInvoiceDetailID`) REFERENCES `salesinvoicedetails` (`SalesInvoiceDetailID`);

--
-- Constraints for table `salesreturns`
--
ALTER TABLE `salesreturns`
  ADD CONSTRAINT `salesreturns_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `customers` (`CustomerID`),
  ADD CONSTRAINT `salesreturns_salesinvoices_SalesInvoiceID_fk` FOREIGN KEY (`SalesInvoiceID`) REFERENCES `salesinvoices` (`SalesInvoiceID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
