-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 22, 2023 at 05:55 AM
-- Server version: 10.4.19-MariaDB
-- PHP Version: 8.0.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cangkir`
--

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `UserID` char(5) NOT NULL,
  `CupID` char(5) NOT NULL,
  `Quantity` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `cart`
--

INSERT INTO `cart` (`UserID`, `CupID`, `Quantity`) VALUES
('US001', 'CU003', 3),
('US001', 'CU006', 1),
('US002', 'CU006', 4),
('US002', 'CU009', 1),
('US003', 'CU002', 1),
('US003', 'CU005', 2),
('US003', 'CU008', 6),
('US004', 'CU003', 2),
('US004', 'CU007', 5),
('US008', 'CU003', 1);

-- --------------------------------------------------------

--
-- Table structure for table `mscourier`
--

CREATE TABLE `mscourier` (
  `CourierID` char(5) NOT NULL,
  `CourierName` varchar(30) NOT NULL,
  `CourierPrice` int(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `mscourier`
--

INSERT INTO `mscourier` (`CourierID`, `CourierName`, `CourierPrice`) VALUES
('CO001', 'JNA', 20000),
('CO002', 'TAKA', 19000),
('CO003', 'LoinParcel', 22000),
('CO004', 'IRX', 30000),
('CO005', 'JINJA', 150000);

-- --------------------------------------------------------

--
-- Table structure for table `mscup`
--

CREATE TABLE `mscup` (
  `CupID` char(5) NOT NULL,
  `CupName` varchar(30) NOT NULL,
  `CupPrice` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `mscup`
--

INSERT INTO `mscup` (`CupID`, `CupName`, `CupPrice`) VALUES
('CU001', 'Porcelain small cup', 15000),
('CU002', 'Porcelain jug', 33000),
('CU003', 'Glass jug', 35000),
('CU004', 'Wooden cup', 8000),
('CU005', 'Ceramic tea cup set', 280000),
('CU006', 'Plastic Jug', 20000),
('CU007', 'Plastic small cup', 12000),
('CU008', 'Plastic normal cup', 17000),
('CU009', 'Japanese tea cup', 100000);

-- --------------------------------------------------------

--
-- Table structure for table `msuser`
--

CREATE TABLE `msuser` (
  `UserID` char(5) NOT NULL,
  `Username` varchar(30) NOT NULL,
  `UserEmail` varchar(50) NOT NULL,
  `UserPassword` varchar(20) NOT NULL,
  `UserGender` varchar(10) NOT NULL,
  `UserRole` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `msuser`
--

INSERT INTO `msuser` (`UserID`, `Username`, `UserEmail`, `UserPassword`, `UserGender`, `UserRole`) VALUES
('US001', 'swiftee', 'swiftee@gmail.com', 'swiftee123', 'Male', 'Admin'),
('US002', 'efren', 'efren@gmail.com', 'efrnnthnl12', 'Male', 'Admin'),
('US003', 'vncnt', 'vincent@gmail.com', 'njnjnjnj33', 'Male', 'User'),
('US004', 'obrt', 'obort@gmail.com', 'makanIk4n', 'Male', 'Admin'),
('US005', 'feryf', 'nandi@gmail.com', 'frryndi22', 'Male', 'User'),
('US006', 'tester', 'tester@gmail.com', 'tester123', 'Female', 'User'),
('US007', 'admintester', 'admin@gmail.com', 'admin123', 'Male', 'Admin'),
('US008', 'cladmin', 'cl@gmail.com', 'cl123456', 'Male', 'Admin'),
('US009', 'jingyuansayang', 'starrail@gmail.com', 'jingyuan123', 'Male', 'User'),
('US010', 'stevebauadmin', 'steve@gmail.com', 'steve123', 'Male', 'Admin'),
('US011', 'dummyuser', 'dummyuser@gmail.com', 'dummyuser123', 'Male', 'User'),
('US012', 'admin', 'admin@gmail.com', 'admin123', 'Male', 'Admin');

-- --------------------------------------------------------

--
-- Table structure for table `transactiondetail`
--

CREATE TABLE `transactiondetail` (
  `TransactionID` char(5) NOT NULL,
  `CupID` char(5) NOT NULL,
  `Quantity` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transactiondetail`
--

INSERT INTO `transactiondetail` (`TransactionID`, `CupID`, `Quantity`) VALUES
('TR001', 'CU001', 3),
('TR001', 'CU002', 2),
('TR002', 'CU008', 2),
('TR003', 'CU005', 1),
('TR004', 'CU007', 10),
('TR004', 'CU008', 2),
('TR005', 'CU002', 32),
('TR006', 'CU001', 5),
('TR007', 'CU008', 4),
('TR008', 'CU009', 2),
('TR009', 'CU008', 7),
('TR010', 'CU009', 2),
('TR012', 'CU008', 1),
('TR013', 'CU006', 1),
('TR014', 'CU003', 1),
('TR014', 'CU008', 1),
('TR015', 'CU006', 1),
('TR015', 'CU009', 4),
('TR016', 'CU009', 1),
('TR017', 'CU002', 1),
('TR017', 'CU009', 2),
('TR018', 'CU002', 6),
('TR018', 'CU006', 2),
('TR018', 'CU008', 4);

-- --------------------------------------------------------

--
-- Table structure for table `transactionheader`
--

CREATE TABLE `transactionheader` (
  `TransactionID` char(5) NOT NULL,
  `UserID` char(5) NOT NULL,
  `CourierID` char(5) NOT NULL,
  `TransactionDate` date NOT NULL,
  `UseDeliveryInsurance` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transactionheader`
--

INSERT INTO `transactionheader` (`TransactionID`, `UserID`, `CourierID`, `TransactionDate`, `UseDeliveryInsurance`) VALUES
('TR001', 'US001', 'CO001', '2023-01-19', 0),
('TR002', 'US001', 'CO002', '2023-01-20', 1),
('TR003', 'US001', 'CO002', '2023-02-19', 1),
('TR004', 'US002', 'CO003', '2022-01-19', 0),
('TR005', 'US003', 'CO004', '2021-01-19', 0),
('TR006', 'US004', 'CO001', '2022-05-19', 1),
('TR007', 'US004', 'CO002', '2022-03-02', 1),
('TR008', 'US004', 'CO002', '2023-04-25', 1),
('TR009', 'US005', 'CO005', '2023-04-10', 0),
('TR010', 'US005', 'CO001', '2022-04-29', 0),
('TR011', 'US001', 'CO004', '2023-05-19', 0),
('TR012', 'US001', 'CO005', '2023-05-19', 0),
('TR013', 'US001', 'CO004', '2023-05-19', 0),
('TR014', 'US001', 'CO001', '2023-05-19', 1),
('TR015', 'US001', 'CO004', '2023-05-20', 1),
('TR016', 'US009', 'CO004', '2023-05-20', 1),
('TR017', 'US010', 'CO001', '2023-05-20', 0),
('TR018', 'US003', 'CO004', '2023-05-22', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`UserID`,`CupID`),
  ADD KEY `CupID` (`CupID`) USING BTREE,
  ADD KEY `UserID` (`UserID`) USING BTREE;

--
-- Indexes for table `mscourier`
--
ALTER TABLE `mscourier`
  ADD PRIMARY KEY (`CourierID`);

--
-- Indexes for table `mscup`
--
ALTER TABLE `mscup`
  ADD PRIMARY KEY (`CupID`);

--
-- Indexes for table `msuser`
--
ALTER TABLE `msuser`
  ADD PRIMARY KEY (`UserID`);

--
-- Indexes for table `transactiondetail`
--
ALTER TABLE `transactiondetail`
  ADD PRIMARY KEY (`TransactionID`,`CupID`),
  ADD KEY `transactiondetail_ibfk_3` (`CupID`);

--
-- Indexes for table `transactionheader`
--
ALTER TABLE `transactionheader`
  ADD PRIMARY KEY (`TransactionID`),
  ADD KEY `transactionheader_ibfk_1` (`UserID`),
  ADD KEY `transactionheader_ibfk_2` (`CourierID`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `msuser` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`CupID`) REFERENCES `mscup` (`CupID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `transactiondetail`
--
ALTER TABLE `transactiondetail`
  ADD CONSTRAINT `FK_detailheader` FOREIGN KEY (`TransactionID`) REFERENCES `transactionheader` (`TransactionID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `transactiondetail_ibfk_3` FOREIGN KEY (`CupID`) REFERENCES `mscup` (`CupID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `transactionheader`
--
ALTER TABLE `transactionheader`
  ADD CONSTRAINT `transactionheader_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `msuser` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `transactionheader_ibfk_2` FOREIGN KEY (`CourierID`) REFERENCES `mscourier` (`CourierID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
