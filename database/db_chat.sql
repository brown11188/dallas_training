-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 02, 2016 at 07:27 AM
-- Server version: 10.1.9-MariaDB
-- PHP Version: 5.5.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_chat`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_message`
--

CREATE TABLE `tbl_message` (
  `message_id` int(11) NOT NULL,
  `message` varchar(225) COLLATE utf8_unicode_ci DEFAULT NULL,
  `expires_time` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `sender_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_user`
--

CREATE TABLE `tbl_user` (
  `user_id` int(11) NOT NULL,
  `user_name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `registration_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `tbl_user`
--

INSERT INTO `tbl_user` (`user_id`, `user_name`, `password`, `registration_id`) VALUES
(1, 'Harold', '4297f44b13955235245b2497399d7a93', 'cIEWB23E2Jw:APA91bGviimW23P1OCi7bAHD6hvcLxW2e9A74uRc2lJIh9wunnK0tkC5pS_haxA_uoiaSGFcmR4jTe5kMdqMer-BS50yfJ6GD0ON7jlXon3yk0382uItEUBBe8gkXCFnhUi9RobBuj2O'),
(2, 'Hawk', '4297f44b13955235245b2497399d7a93', 'cCTBwELYhjs:APA91bGdxF72vrJmxdPx281P_czcdCMN_I_aULsRwwZFZamyVUNZ_CC-pNJYM5r-u8w2cto5XkeLAdPdQddPaP2DKlU2CMec6Ld6-CBk8xQ3RYTq9M2hJnNyPowlG8wV_MceXcQ6Kv-Y'),
(5, 'Hawk2', '4297f44b13955235245b2497399d7a93', 'cCTBwELYhjs:APA91bGdxF72vrJmxdPx281P_czcdCMN_I_aULsRwwZFZamyVUNZ_CC-pNJYM5r-u8w2cto5XkeLAdPdQddPaP2DKlU2CMec6Ld6-CBk8xQ3RYTq9M2hJnNyPowlG8wV_MceXcQ6Kv-Y');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_message`
--
ALTER TABLE `tbl_message`
  ADD PRIMARY KEY (`message_id`);

--
-- Indexes for table `tbl_user`
--
ALTER TABLE `tbl_user`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_message`
--
ALTER TABLE `tbl_message`
  MODIFY `message_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `tbl_user`
--
ALTER TABLE `tbl_user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
