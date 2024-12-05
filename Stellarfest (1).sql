-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 04, 2024 at 05:52 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `stellarfest`
--

-- --------------------------------------------------------

--
-- Table structure for table `events`
--

CREATE TABLE `events` (
  `event_id` varchar(6) NOT NULL CHECK (`event_id` regexp '^EV[0-9]{3}$'),
  `event_name` varchar(255) NOT NULL,
  `event_date` date NOT NULL,
  `event_location` varchar(255) NOT NULL,
  `event_description` text DEFAULT NULL,
  `organizer_id` varchar(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `events`
--

INSERT INTO `events` (`event_id`, `event_name`, `event_date`, `event_location`, `event_description`, `organizer_id`) VALUES
('EV001', 'Tech Conference 2024', '2024-12-20', 'Jakarta', 'A technology-focused conference.', 'US001'),
('EV002', 'Startup Meetup', '2024-12-21', 'Bandung', 'Networking event for startups.', 'US001');

-- --------------------------------------------------------

--
-- Table structure for table `invitations`
--

CREATE TABLE `invitations` (
  `invitation_id` varchar(6) NOT NULL CHECK (`invitation_id` regexp '^IN[0-9]{3}$'),
  `event_id` varchar(6) NOT NULL,
  `user_id` varchar(6) NOT NULL,
  `invitation_status` varchar(10) NOT NULL CHECK (`invitation_status` in ('Pending','Accepted','Declined')),
  `invitation_role` varchar(10) NOT NULL CHECK (`invitation_role` in ('Guest','Vendor'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `invitations`
--

INSERT INTO `invitations` (`invitation_id`, `event_id`, `user_id`, `invitation_status`, `invitation_role`) VALUES
('IN001', 'EV001', 'US002', 'Pending', 'Guest'),
('IN002', 'EV001', 'US003', 'Accepted', 'Vendor'),
('IN003', 'EV002', 'US002', 'Accepted', 'Guest');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_id` varchar(6) NOT NULL CHECK (`product_id` regexp '^PR[0-9]{3}$'),
  `product_name` varchar(255) NOT NULL,
  `product_description` varchar(200) DEFAULT NULL,
  `user_id` varchar(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `product_name`, `product_description`, `user_id`) VALUES
('PR001', 'Wireless Mouse', 'A high-quality wireless mouse with ergonomic design.', 'US003'),
('PR002', 'Gaming Keyboard', 'RGB mechanical keyboard with customizable macros.', 'US003');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` varchar(6) NOT NULL CHECK (`user_id` regexp '^US[0-9]{3}$'),
  `user_name` varchar(255) NOT NULL,
  `user_email` varchar(255) NOT NULL,
  `user_password` varchar(255) NOT NULL,
  `user_role` varchar(15) NOT NULL CHECK (`user_role` in ('Admin','Guest','Vendor','EventOrganizer'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `user_name`, `user_email`, `user_password`, `user_role`) VALUES
('US001', 'Alice', 'alice@example.com', 'securepassword1', 'EventOrganizer'),
('US002', 'Bob', 'bob@example.com', 'securepassword2', 'Guest'),
('US003', 'Charlie', 'charlie@example.com', 'securepassword3', 'Vendor'),
('US004', 'Diana', 'diana@example.com', 'securepassword4', 'Admin');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `events`
--
ALTER TABLE `events`
  ADD PRIMARY KEY (`event_id`),
  ADD KEY `organizer_id` (`organizer_id`);

--
-- Indexes for table `invitations`
--
ALTER TABLE `invitations`
  ADD PRIMARY KEY (`invitation_id`),
  ADD KEY `event_id` (`event_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `user_email` (`user_email`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `events`
--
ALTER TABLE `events`
  ADD CONSTRAINT `events_ibfk_1` FOREIGN KEY (`organizer_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `invitations`
--
ALTER TABLE `invitations`
  ADD CONSTRAINT `invitations_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `invitations_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `Users` (`user_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
