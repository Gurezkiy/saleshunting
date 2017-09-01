-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Авг 24 2017 г., 20:23
-- Версия сервера: 5.5.25
-- Версия PHP: 5.3.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `fox`
--

-- --------------------------------------------------------

--
-- Структура таблицы `messages`
--

CREATE TABLE IF NOT EXISTS `messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_user_id` int(11) NOT NULL,
  `to_user_id` int(11) NOT NULL,
  `message` text NOT NULL,
  `read` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`from_user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Дамп данных таблицы `messages`
--

INSERT INTO `messages` (`id`, `from_user_id`, `to_user_id`, `message`, `read`, `created_at`) VALUES
(1, 21, 22, 'Hello)', 1, 1503497700),
(2, 22, 21, 'Hi)', 0, 1503497730);

-- --------------------------------------------------------

--
-- Структура таблицы `sales`
--

CREATE TABLE IF NOT EXISTS `sales` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `value` float NOT NULL,
  `show` int(11) NOT NULL,
  `whom_user` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Дамп данных таблицы `sales`
--

INSERT INTO `sales` (`id`, `user_id`, `value`, `show`, `whom_user`) VALUES
(1, 21, 6.5, 1, 21);

-- --------------------------------------------------------

--
-- Структура таблицы `tokens`
--

CREATE TABLE IF NOT EXISTS `tokens` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `token` text NOT NULL,
  `time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=27 ;

--
-- Дамп данных таблицы `tokens`
--

INSERT INTO `tokens` (`id`, `user_id`, `token`, `time`) VALUES
(25, 22, 'fox_token_599b015c5068f1.23436990', 1503330652),
(26, 21, 'fox_token_599b01bb0f66d9.35056670', 1503330747);

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `avatar` text NOT NULL,
  `phone` text NOT NULL,
  `birthday` bigint(20) NOT NULL,
  `password` text NOT NULL,
  `access_level` int(11) NOT NULL DEFAULT '0',
  `pet` int(11) NOT NULL DEFAULT '0',
  `ban` int(11) NOT NULL DEFAULT '0',
  `sms_code` int(11) NOT NULL,
  `active` int(11) NOT NULL DEFAULT '0',
  `device_id` text NOT NULL,
  `gcm_registration_id` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=23 ;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`id`, `name`, `avatar`, `phone`, `birthday`, `password`, `access_level`, `pet`, `ban`, `sms_code`, `active`, `device_id`, `gcm_registration_id`) VALUES
(21, 'Gurezkiy', 'uploads/images/1503232188.jpg', '375292696923', 814334400, '493fba3822b015c5e3fc31def30654f7', 0, 0, 0, 16413, 1, '15f86708f7244dc9', ''),
(22, 'Nick Gurezkiy', '', '375292696925', 814334400, '926d06983ecd1ffdb7243de4672bd087', 0, 0, 0, 96097, 1, '15f86708f7244dc9', '');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
