-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Client :  localhost
-- Généré le :  Mar 31 Décembre 2024 à 20:01 par FATMA HAMMEDI
-- Version du serveur :  5.6.20-log
-- Version de PHP :  5.4.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `mydatabase1`
--

-- --------------------------------------------------------

--
-- Structure de la table `administrateur`
--

CREATE TABLE IF NOT EXISTS `administrateur` (
  `id_admin` int(11) NOT NULL,
  `nom_admin` varchar(50) NOT NULL,
  `prenom_admin` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL
) ENGINE=MyISAM  DEFAULT CHARSET=latin1;

--
-- Contenu de la table `administrateur`
--

INSERT INTO `administrateur` (`id_admin`, `nom_admin`, `prenom_admin`, `email`) VALUES
(4, 'admin1', 'Systeme', 'admin1@ensit.tn'),
(4, 'admin1', 'Systeme', 'admin1@ensit.tn');

-- --------------------------------------------------------

--
-- Structure de la table `classe`
--

CREATE TABLE IF NOT EXISTS `classe` (
  `id_classe` varchar(255) NOT NULL DEFAULT '',
  `nom_classe` varchar(255) NOT NULL,
  `niveau` varchar(20) NOT NULL,
  `section` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `classe`
--

INSERT INTO `classe` (`id_classe`, `nom_classe`, `niveau`, `section`) VALUES
('1Gmam', '1ére Génie Mathématique ', '1', 'Génie Mathématique '),
('2Gelec', '2éme Génie Électrique', '2', 'Génie Électrique'),
('2Ginfo', '2 éme Génie Informatique', '2', 'Génie Informatique'),
('2Gmam', ' 2éme Génie Mathématique', '2', 'Génie Mathématique'),
('3Gmam', '3éme Génie Mathématique', '3', ' Génie Mathématique');

-- --------------------------------------------------------

--
-- Structure de la table `dispense`
--

CREATE TABLE IF NOT EXISTS `dispense` (
  `id_ens` int(11) NOT NULL,
  `id_classe` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Contenu de la table `dispense`
--

INSERT INTO `dispense` (`id_ens`, `id_classe`) VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 3),
(3, 2),
(3, 3),
(4, 1),
(4, 3),
(5, 1),
(5, 2),
(6, 1),
(6, 3),
(7, 2),
(7, 3),
(8, 1),
(8, 3),
(9, 2),
(9, 3),
(10, 1),
(10, 2);

-- --------------------------------------------------------

--
-- Structure de la table `enseignant`
--

CREATE TABLE IF NOT EXISTS `enseignant` (
`id_ens` int(11) NOT NULL,
  `nom_ens` varchar(20) NOT NULL,
  `prenom_ens` varchar(20) NOT NULL,
  `Grade` varchar(20) NOT NULL,
  `specialite` varchar(20) NOT NULL,
  `email` varchar(40) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=19 ;

--
-- Contenu de la table `enseignant`
--

INSERT INTO `enseignant` (`id_ens`, `nom_ens`, `prenom_ens`, `Grade`, `specialite`, `email`) VALUES
(1, 'sayadi', 'mounir', 'professeur', 'probabilite', 'mounir.sayadi@ensit.tn'),
(2, 'mbarki', 'mahfoudh', 'Professeur Assistant', 'java', 'mahfoudh.mbarki@ensit.tn'),
(3, 'drissi', 'dorra', 'professeur', 'edp', 'dorra.drissi@ensit.tn'),
(4, 'berzig', 'moez', 'professeur', 'analyse numerique', 'moez.bersig@ensit.tn'),
(5, 'kasmi', 'sofiane', 'professeur', 'stat', 'sofiane.kasmi@ensit.tn'),
(6, 'aboudi', 'mohamed_ali ', 'Professeur Assistant', 'Réseaux Informatique', 'mohamed_ali.aboudi@ensit.tn'),
(7, 'ben_rgaya', 'houde', 'Doctorat', 'web', 'houde.ben_rgaya@ensit.tn'),
(8, 'abid', 'sabeur', 'professeur', 'optimisatin', 'sabeur.abid@ensit.tn'),
(9, 'tounsi', 'radouane', 'Professeur Assistant', 'R', 'radouane.tounsi@ensit.tn'),
(10, 'yaacoubi', 'adnéne', 'professeur', 'élément finie', 'adnéne.yaacoubi@ensit.tn'),
(11, 'abid', 'sabeur', 'professeur', 'optimisatin', 'sabeur.abid@ensit.tn'),
(18, 'sedik', 'hassene', 'Professeur', 'directeur', 'sedik.hassen@ensit.tn');

-- --------------------------------------------------------

--
-- Structure de la table `enseigne`
--

CREATE TABLE IF NOT EXISTS `enseigne` (
  `id_ens` int(11) NOT NULL DEFAULT '0',
  `id_matiere` int(11) NOT NULL DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Contenu de la table `enseigne`
--

INSERT INTO `enseigne` (`id_ens`, `id_matiere`) VALUES
(1, 1),
(2, 3);

-- --------------------------------------------------------

--
-- Structure de la table `etudiant`
--

CREATE TABLE IF NOT EXISTS `etudiant` (
`id_etud` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `date_naissance` date NOT NULL,
  `email` varchar(100) NOT NULL,
  `id_classe` varchar(255) DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=94 ;

--
-- Contenu de la table `etudiant`
--

INSERT INTO `etudiant` (`id_etud`, `nom`, `prenom`, `date_naissance`, `email`, `id_classe`) VALUES
(1, 'Karoui', 'Manel', '2000-06-29', 'manel.karoui@ensit.tn', '2Gmam'),
(2, 'Smei', 'Youssef', '2001-03-15', 'youssef.smei@ensit.tn', '2Gmam'),
(3, 'Tati', 'Youssef', '2001-06-01', 'youssef.tati@ensit.tn', '2Gmam'),
(4, 'Bendaoud', 'Ranim', '2001-03-12', 'ranim.bendaoud@ensit.tn', '2Gmam'),
(5, 'Boufeid', 'Adem', '2003-05-01', 'adem.boufaid@ensit.tn', '2Gmam'),
(6, 'zaghdoudi', 'Nidhal', '2000-11-25', 'nidhal.zaghdoudia@ensit.tn', '2Gmam'),
(7, 'Riahi', 'Sana', '2002-01-30', 'sana.riahi@ensit.tn', '2Gmam'),
(8, 'Benhamdoune', 'Eya', '2001-02-15', 'eya.benhamdoune@ensit.tn', '2Gmam'),
(9, 'Maktouf', 'Karim', '2002-06-10', 'karim.maktouf@ensit.tn', '2Gmam'),
(10, 'Benmansour', 'Islem', '2002-09-05', 'islem.benmansour@ensit.tn', '2Gmam'),
(11, 'Belgith', 'Farah', '2003-03-18', 'farah.belgith@ensit.tn', '1Gmam'),
(12, 'Gdara', 'Dhia', '2003-08-25', 'dhia.gdara@ensit.tn', '1Gmam'),
(13, 'Ganoun', 'Nadia', '2001-01-10', 'nadia.ganoun@ensit.tn', '1Gmam'),
(14, 'Omrani', 'Hiba', '2003-05-08', 'hiba.omrani@ensit.tn', '1Gmam'),
(15, 'Kacem', 'Tarek', '2003-12-12', 'tarek.kacem@ensit.tn', '1Gmam'),
(16, 'Khadhar', 'Rim', '2003-04-22', 'rim.khadhar@ensit.tn', '1Gmam'),
(17, 'Oueslati', 'Wassim', '2003-07-15', 'wassim.oueslati@ensit.tn', '1Gmam'),
(18, 'Souid', 'Myriam', '2003-09-01', 'myriam.souid@ensit.tn', '1Gmam'),
(19, 'Slimani', 'Rami', '2003-10-18', 'rami.slimani@ensit.tn', '1Gmam'),
(20, 'Drissi', 'Rania', '2003-12-30', 'rania.drissi@ensit.tn', '1Gmam'),
(21, 'Jebali', 'Nizar', '2000-02-14', 'nizar.jebali@ensit.tn', '3'),
(22, 'Ben Salah', 'Amina', '2000-04-18', 'amina.bensalah@ensit.tn', '3'),
(23, 'Khouaja', 'Fares', '2000-07-20', 'fares.khouaja@ensit.tn', '3'),
(24, 'Bouhlel', 'Syrine', '2001-02-25', 'syrine.bouhlel@ensit.tn', '3'),
(25, 'Maalej', 'Iyed', '2001-06-06', 'iyed.maalej@ensit.tn', '3'),
(26, 'Chaouch', 'Nour', '2001-11-08', 'nour.chaouch@ensit.tn', '3'),
(27, 'Abdelkafi', 'Walid', '2002-01-15', 'walid.abdelkafi@ensit.tn', '3'),
(28, 'Ben Farhat', 'Sarra', '2002-03-30', 'sarra.benfarhat@ensit.tn', '3'),
(29, 'Ben Hassen', 'Bilel', '2002-07-05', 'bilel.benhassen@ensit.tn', '3'),
(30, 'Toumi', 'Malek', '2002-10-10', 'malek.toumi@ensit.tn', '3'),
(32, 'Hammedi', 'Fatma', '2001-06-07', 'fatma.hammedi@ensit.tn', '2Gmam'),
(65, 'Ben Salah', 'Amir', '2005-12-10', 'amir.bensalah@ensit.tn', '2Gmam'),
(67, 'Mabrouk', 'Maya', '2006-07-17', 'maya.mabrouk@ensit.tn', '2Gmam'),
(68, 'Laaroussi', 'Salah', '2007-09-05', 'salah.laaroussi@ensit.tn', '2Gmam'),
(69, 'Bouaziz', 'Leila', '2006-06-13', 'leila.bouaziz@ensit.tn', '2Gmam'),
(70, 'Mejri', 'Ali', '2006-01-22', 'ali.mejri@ensit.tn', '2Gmam'),
(71, 'Zouari', 'Hassine', '2005-05-30', 'hassine.zouari@ensit.tn', '2Gmam'),
(83, 'elkhasen', 'yasmina', '2001-06-06', 'yasmina.elkhazen@ensit.tn', '3Gmam'),
(84, 'arfaoui', 'rihem', '2001-07-09', 'arfaoui.rihem@ensit.tn', '3Gmam'),
(85, 'hamdouni', 'achref', '2001-07-09', 'achref.hamdouni@ensit.tn', '3Gmam'),
(86, 'ati', 'rythem', '2001-08-08', 'ryhem.ati@ensit.tn', '3Gmam'),
(87, 'bchir', 'amir', '2000-10-09', 'amir.bchir@ensit.tn', '3Gmam'),
(88, 'abed', 'louay', '2000-10-07', 'louay.bchir@ensit.tn', '3Gmam'),
(89, 'hsini', 'samar', '2000-11-09', 'samar.hsini@ensit.tn', '3Gmam'),
(90, 'rebhi', 'mohsen', '2001-07-09', 'rebhi.mohsen@ensit.tn', '3Gmam'),
(91, 'jawedi', 'amine', '2000-08-09', 'amine.jawedi@ensit.tn', '3Gmam'),
(92, 'zaidoun', 'nawres', '2000-12-07', 'nawres.zaidoun@ensit.tn', '3Gmam'),
(93, 'sadek', 'feyza', '2001-11-09', 'feyza.sadek@ensit.tn', '3Gmam');

-- --------------------------------------------------------

--
-- Structure de la table `etudier`
--

CREATE TABLE IF NOT EXISTS `etudier` (
  `id_etud` int(11) NOT NULL DEFAULT '0',
  `id_matiere` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `etudier`
--

INSERT INTO `etudier` (`id_etud`, `id_matiere`) VALUES
(1, 1),
(2, 1),
(1, 1),
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(5, 1),
(6, 1),
(7, 1),
(8, 1),
(9, 1),
(10, 1),
(2, 2),
(2, 2),
(32, 1),
(32, 1),
(11, 1),
(12, 1),
(13, 1),
(14, 1),
(15, 1),
(16, 1),
(17, 1),
(18, 1),
(18, 1),
(19, 1),
(20, 1),
(1, 2),
(1, 2),
(1, 2),
(1, 2),
(1, 2),
(1, 2),
(1, 2),
(1, 2),
(3, 2),
(4, 2),
(5, 2),
(6, 2),
(7, 2),
(8, 2),
(9, 2),
(10, 2),
(1, 2),
(3, 2),
(4, 2),
(5, 2),
(6, 2),
(7, 2),
(8, 2),
(9, 2),
(10, 2),
(32, 2),
(32, 2),
(11, 3),
(11, 3),
(12, 3),
(13, 3),
(14, 3),
(15, 3),
(16, 3),
(17, 3),
(18, 3),
(19, 3),
(20, 3),
(83, 5),
(83, 5),
(83, 5),
(84, 5),
(85, 5),
(86, 5),
(87, 5),
(88, 5),
(89, 5),
(90, 5),
(91, 5),
(92, 5),
(93, 5);

-- --------------------------------------------------------

--
-- Structure de la table `matiere`
--

CREATE TABLE IF NOT EXISTS `matiere` (
`id_matiere` int(11) NOT NULL,
  `nom_matiere` varchar(20) NOT NULL,
  `description` varchar(20) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Contenu de la table `matiere`
--

INSERT INTO `matiere` (`id_matiere`, `nom_matiere`, `description`) VALUES
(1, 'probabilité', 'regime mixte'),
(2, 'java', 'regime mixte'),
(3, 'stat', 'regime mixte'),
(5, 'AI', 'régime mixte ');

-- --------------------------------------------------------

--
-- Structure de la table `typedesession`
--

CREATE TABLE IF NOT EXISTS `typedesession` (
  `id_matiere` int(11) NOT NULL,
  `id_etud` int(11) NOT NULL,
  `DS` float DEFAULT NULL,
  `TP` float NOT NULL,
  `Examen` float NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Contenu de la table `typedesession`
--

INSERT INTO `typedesession` (`id_matiere`, `id_etud`, `DS`, `TP`, `Examen`) VALUES
(1, 32, 18.5, 20, 18),
(1, 10, 12, 0, 17),
(1, 9, 13, 0, 17),
(1, 8, 12, 0, 19.5),
(1, 7, 13, 0, 12),
(1, 6, 13.5, 0, 17),
(1, 5, 13, 0, 17.5),
(1, 4, 12, 0, 17),
(1, 3, 13, 0, 16),
(1, 2, 12, 0, 13),
(1, 1, 13, 0, 16),
(2, 2, 0, 0, 0),
(1, 11, 0, 0, 14),
(1, 12, 0, 0, 0),
(1, 13, 0, 0, 0),
(1, 14, 0, 0, 0),
(1, 15, 0, 0, 0),
(1, 16, 0, 0, 0),
(1, 17, 0, 0, 0),
(1, 18, 0, 0, 0),
(1, 19, 0, 0, 0),
(1, 20, 0, 0, 18.5),
(2, 1, 0, 0, 0),
(2, 32, 0, 0, 0),
(2, 3, 0, 0, 0),
(2, 4, 0, 0, 0),
(2, 5, 0, 0, 0),
(2, 6, 0, 0, 0),
(2, 7, 0, 0, 0),
(2, 8, 0, 0, 0),
(2, 9, 0, 0, 0),
(2, 10, 0, 0, 0),
(3, 11, 0, 0, 0),
(3, 12, 0, 0, 0),
(3, 13, 0, 0, 0),
(3, 14, 0, 0, 0),
(3, 15, 0, 0, 0),
(3, 16, 0, 0, 0),
(3, 17, 0, 0, 0),
(3, 18, 0, 0, 0),
(3, 19, 0, 0, 0),
(3, 20, 0, 0, 0),
(5, 83, 0, 0, 0),
(5, 84, 16, 0, 0),
(5, 85, 0, 0, 0),
(5, 86, 0, 0, 0),
(5, 87, 0, 0, 0),
(5, 88, 18, 0, 0),
(5, 89, 0, 0, 0),
(5, 90, 0, 0, 0),
(5, 91, 0, 0, 0),
(5, 92, 0, 0, 0),
(5, 93, 0, 0, 0);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateurs`
--

CREATE TABLE IF NOT EXISTS `utilisateurs` (
`id_utilisateur` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('enseignant','administrateur') NOT NULL,
  `id_ens` int(11) DEFAULT NULL,
  `id_admin` int(11) DEFAULT NULL
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Contenu de la table `utilisateurs`
--

INSERT INTO `utilisateurs` (`id_utilisateur`, `username`, `password`, `role`, `id_ens`, `id_admin`) VALUES
(1, 'sayadi', '1234', 'enseignant', 1, NULL),
(3, 'sofiane', '123456', 'enseignant', 2, NULL),
(5, 'admin1', '1231', 'administrateur', NULL, 4),
(4, 'samir', '12345678', 'administrateur', NULL, 5);

--
-- Index pour les tables exportées
--

--
-- Index pour la table `classe`
--
ALTER TABLE `classe`
 ADD PRIMARY KEY (`id_classe`);

--
-- Index pour la table `dispense`
--
ALTER TABLE `dispense`
 ADD PRIMARY KEY (`id_ens`,`id_classe`), ADD KEY `id_classe` (`id_classe`);

--
-- Index pour la table `enseignant`
--
ALTER TABLE `enseignant`
 ADD PRIMARY KEY (`id_ens`);

--
-- Index pour la table `enseigne`
--
ALTER TABLE `enseigne`
 ADD PRIMARY KEY (`id_ens`,`id_matiere`), ADD KEY `id_classe` (`id_matiere`);

--
-- Index pour la table `etudiant`
--
ALTER TABLE `etudiant`
 ADD PRIMARY KEY (`id_etud`), ADD UNIQUE KEY `Email` (`email`), ADD KEY `id_classe` (`id_classe`);

--
-- Index pour la table `matiere`
--
ALTER TABLE `matiere`
 ADD PRIMARY KEY (`id_matiere`);

--
-- Index pour la table `typedesession`
--
ALTER TABLE `typedesession`
 ADD PRIMARY KEY (`id_matiere`,`id_etud`), ADD UNIQUE KEY `id_matiere` (`id_matiere`,`id_etud`), ADD KEY `fk_id_etud` (`id_etud`);

--
-- Index pour la table `utilisateurs`
--
ALTER TABLE `utilisateurs`
 ADD PRIMARY KEY (`id_utilisateur`), ADD UNIQUE KEY `Username` (`username`), ADD KEY `id_ens` (`id_ens`), ADD KEY `id_admin` (`id_admin`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `enseignant`
--
ALTER TABLE `enseignant`
MODIFY `id_ens` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=19;
--
-- AUTO_INCREMENT pour la table `etudiant`
--
ALTER TABLE `etudiant`
MODIFY `id_etud` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=94;
--
-- AUTO_INCREMENT pour la table `matiere`
--
ALTER TABLE `matiere`
MODIFY `id_matiere` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT pour la table `utilisateurs`
--
ALTER TABLE `utilisateurs`
MODIFY `id_utilisateur` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
