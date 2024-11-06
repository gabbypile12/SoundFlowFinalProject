-- Running this script will DELETE the existing database and all data it contains.
-- Use with caution.

DROP DATABASE IF EXISTS soundFlowTest;

CREATE DATABASE soundFlowTest;

USE soundFlowTest;

-- -----------------------------------------------------
-- Table `soundFlow`.`Playlist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `soundFlowTest`.`Playlist` (
  `playlistId` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`playlistId`))
ENGINE = InnoDB;
  
  -- -----------------------------------------------------
-- Table `soundFlow`.`Song`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `soundFlowTest`.`Song` (
  `songId` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `previewUrl` VARCHAR(70) NULL,
  `spotifyId` VARCHAR(70) NULL,
  `albumName` VARCHAR(45) NULL,
  PRIMARY KEY (`songId`)
  );
  
-- -----------------------------------------------------
-- Table `soundFlow`.`Artist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `soundFlowTest`.`Artist` (
  `artistId` INT NOT NULL AUTO_INCREMENT,
  `fullName` VARCHAR(45) NOT NULL,
  `imageUrl` VARCHAR(70) NULL,
  `genre` VARCHAR(45) NULL,
  `spotifyId` VARCHAR(70) NULL,
  PRIMARY KEY (`artistId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `soundFlow`.`PlaylistSong`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `soundFlowTest`.`PlaylistSong` (
  `playlistId` INT NOT NULL,
  `songId` INT NOT NULL,
  PRIMARY KEY (`playlistId`, `songId`),
  INDEX `fk_Playlist_has_Song_Song1_idx` (`songId` ASC) VISIBLE,
  INDEX `fk_Playlist_has_Song_Playlist_idx` (`playlistId` ASC) VISIBLE,
  CONSTRAINT `fk_Playlist_has_Song_Playlist`
    FOREIGN KEY (`playlistId`)
    REFERENCES `soundFlow`.`Playlist` (`playlistId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Playlist_has_Song_Song1`
    FOREIGN KEY (`songId`)
    REFERENCES `soundFlow`.`Song` (`songId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `soundFlow`.`ArtistSong`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `soundFlowTest`.`ArtistSong` (
  `artistId` INT NOT NULL,
  `songId` INT NOT NULL,
  PRIMARY KEY (`artistId`, `songId`),
  INDEX `fk_Artist_has_Song_Song1_idx` (`songId` ASC) VISIBLE,
  INDEX `fk_Artist_has_Song_Artist1_idx` (`artistId` ASC) VISIBLE,
  CONSTRAINT `fk_Artist_has_Song_Artist1`
    FOREIGN KEY (`artistId`)
    REFERENCES `soundFlow`.`Artist` (`artistId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Artist_has_Song_Song1`
    FOREIGN KEY (`songId`)
    REFERENCES `soundFlow`.`Song` (`songId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- ------------------------------
-- 		MOCK DATA
-- ------------------------------

