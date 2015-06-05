CREATE TABLE AppNode
(
  id            INTEGER PRIMARY KEY NOT NULL,
  nameOrAddress TEXT,
  guid          TEXT                NOT NULL,
  appType       TEXT                NOT NULL,
  hostingType   TEXT                NOT NULL
);

CREATE TABLE Asset
(
  id           INTEGER PRIMARY KEY NOT NULL,
  name         TEXT                NOT NULL,
  assetType    TEXT,
  assetStatus  TEXT,
  programId    INTEGER,
  fileSystemId INTEGER,
  FOREIGN KEY (programId) REFERENCES Program (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (fileSystemId) REFERENCES FileSystem (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE AssetPart
(
  id              INTEGER PRIMARY KEY NOT NULL,
  name            TEXT                NOT NULL,
  assetPartType   TEXT,
  assetPartStatus TEXT,
  containerFormat TEXT,
  assetId         INTEGER,
  FOREIGN KEY (assetId) REFERENCES Asset (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE AssetPart_File
(
  assetPartId INTEGER,
  fileId      INTEGER,
  FOREIGN KEY (fileId) REFERENCES File (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (assetPartId) REFERENCES AssetPart (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE AssetPart_MetadataCollection
(
  assetPartId          INTEGER,
  metadataCollectionId INTEGER,
  FOREIGN KEY (metadataCollectionId) REFERENCES MetadataCollection (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (assetPartId) REFERENCES AssetPart (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Asset_MetadataCollection
(
  assetId              INTEGER,
  metadataCollectionId INTEGER,
  FOREIGN KEY (metadataCollectionId) REFERENCES MetadataCollection (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (assetId) REFERENCES Asset (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE AudioTrack
(
  id                   INTEGER PRIMARY KEY NOT NULL,
  bitRate              INTEGER,
  sampleRate           INTEGER,
  language             TEXT,
  audioCodec           TEXT,
  audioCodecParameters TEXT,
  noOfChannels         TEXT,
  durationMillis       INTEGER,
  FOREIGN KEY (id) REFERENCES Track (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Bill
(
  id                   INTEGER PRIMARY KEY NOT NULL,
  name                 TEXT                NOT NULL,
  description          TEXT,
  creationDate         INTEGER             NOT NULL,
  dueDate              INTEGER             NOT NULL,
  paymentStatus        TEXT,
  beginDate            INTEGER,
  endDate              INTEGER,
  pricePolicyId        INTEGER,
  billedOrganizationId INTEGER,
  totalAmount          REAL,
  discount             REAL,
  billedAmount         REAL,
  paidAmount           REAL,
  comments             TEXT,
  FOREIGN KEY (pricePolicyId) REFERENCES PricePolicy (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (billedOrganizationId) REFERENCES Organization (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE BillItem
(
  id                INTEGER PRIMARY KEY NOT NULL,
  creationDate      INTEGER             NOT NULL,
  billId            INTEGER,
  pricePolicyItemId INTEGER,
  lineNo            INTEGER,
  price             REAL,
  qty               INTEGER,
  amount            REAL,
  comments          TEXT,
  FOREIGN KEY (pricePolicyItemId) REFERENCES PricePolicyItem (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (billId) REFERENCES Bill (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE BillItem_RecordableEvent
(
  billItemId        INTEGER,
  recordableEventId INTEGER,
  FOREIGN KEY (recordableEventId) REFERENCES RecordableEvent (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (billItemId) REFERENCES BillItem (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE CCTrack
(
  id       INTEGER PRIMARY KEY NOT NULL,
  language TEXT,
  format   TEXT,
  FOREIGN KEY (id) REFERENCES Track (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE FTPStorageLocation
(
  userName   TEXT                NOT NULL,
  password   TEXT                NOT NULL,
  id         INTEGER PRIMARY KEY NOT NULL,
  hostName   TEXT                NOT NULL,
  portNumber INTEGER             NOT NULL,
  passive    INTEGER             NOT NULL,
  FOREIGN KEY (id) REFERENCES StorageLocation (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE File
(
  id       INTEGER PRIMARY KEY NOT NULL,
  fileSize INTEGER,
  FOREIGN KEY (id) REFERENCES FileSystemObject (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE FileSystem
(
  id                INTEGER PRIMARY KEY NOT NULL,
  storageLocationId INTEGER             NOT NULL,
  name              TEXT                NOT NULL,
  FOREIGN KEY (storageLocationId) REFERENCES StorageLocation (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE FileSystemObject
(
  id                    INTEGER PRIMARY KEY NOT NULL,
  type                  TEXT,
  fileSystemId          INTEGER             NOT NULL,
  createdTimestamp      INTEGER,
  modifiedTimestamp     INTEGER,
  lastAccessedTimestamp INTEGER,
  name                  TEXT                NOT NULL,
  FOREIGN KEY (fileSystemId) REFERENCES FileSystem (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE FileSystem_RootFolder
(
  fileSystemId INTEGER,
  rootFolderId INTEGER,
  FOREIGN KEY (rootFolderId) REFERENCES Folder (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (fileSystemId) REFERENCES FileSystem (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Folder
(
  id       INTEGER PRIMARY KEY NOT NULL,
  fullPath TEXT,
  FOREIGN KEY (id) REFERENCES FileSystemObject (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Folder_FileSystemObject
(
  folderId           INTEGER,
  fileSystemObjectId INTEGER,
  FOREIGN KEY (folderId) REFERENCES Folder (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (fileSystemObjectId) REFERENCES FileSystemObject (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE MetadataCollection
(
  id INTEGER PRIMARY KEY NOT NULL
);

CREATE TABLE MetadataCollectionTemplate
(
  id   INTEGER PRIMARY KEY NOT NULL,
  name TEXT                NOT NULL
);

CREATE TABLE MetadataName
(
  id                           INTEGER PRIMARY KEY NOT NULL,
  name                         TEXT                NOT NULL,
  dataType                     TEXT                NOT NULL,
  metadataCollectionTemplateId INTEGER,
  FOREIGN KEY (metadataCollectionTemplateId) REFERENCES MetadataCollectionTemplate (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE MetadataValue
(
  id                   INTEGER PRIMARY KEY NOT NULL,
  metadataNameId       INTEGER,
  metadataCollectionId INTEGER,
  value                TEXT                NOT NULL,
  FOREIGN KEY (metadataNameId) REFERENCES MetadataName (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (metadataCollectionId) REFERENCES MetadataCollection (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Organization
(
  id          INTEGER PRIMARY KEY NOT NULL,
  name        TEXT,
  mailAddress TEXT,
  webSite     TEXT,
  orgType     TEXT                NOT NULL
);

CREATE TABLE PricePolicy
(
  id            INTEGER PRIMARY KEY NOT NULL,
  name          TEXT                NOT NULL,
  description   TEXT,
  creationDate  INTEGER             NOT NULL,
  inEffect      INTEGER             NOT NULL,
  inEffectSince INTEGER
);

CREATE TABLE PricePolicyItem
(
  id            INTEGER PRIMARY KEY NOT NULL,
  name          TEXT                NOT NULL,
  description   TEXT,
  pricePolicyId INTEGER,
  metricsType   TEXT                NOT NULL,
  fromQty       INTEGER,
  toQty         INTEGER,
  price         REAL,
  FOREIGN KEY (pricePolicyId) REFERENCES PricePolicy (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE PricePolicy_Organization
(
  pricePolicyId  INTEGER,
  organizationId INTEGER,
  FOREIGN KEY (pricePolicyId) REFERENCES PricePolicy (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (organizationId) REFERENCES Organization (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Program
(
  id          INTEGER PRIMARY KEY NOT NULL,
  name        TEXT                NOT NULL,
  programType TEXT,
  channel     TEXT
);

CREATE TABLE Program_MetadataCollection
(
  programId            INTEGER,
  metadataCollectionId INTEGER,
  FOREIGN KEY (programId) REFERENCES Program (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (metadataCollectionId) REFERENCES MetadataCollection (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Program_Organization
(
  programId      INTEGER,
  organizationId INTEGER,
  FOREIGN KEY (programId) REFERENCES Program (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (organizationId) REFERENCES Organization (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE RecordableEvent
(
  id             INTEGER PRIMARY KEY NOT NULL,
  sentAt         INTEGER,
  type           TEXT,
  receivedAt     INTEGER             NOT NULL,
  category       TEXT                NOT NULL,
  organizationId INTEGER,
  userId         INTEGER,
  FOREIGN KEY (userId) REFERENCES User (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (organizationId) REFERENCES Organization (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Role
(
  id          INTEGER PRIMARY KEY NOT NULL,
  name        TEXT,
  "default"   INTEGER,
  permissions INTEGER
);

CREATE UNIQUE INDEX ix_Role_default ON Role ("default");
CREATE TABLE Role_User
(
  roleId INTEGER,
  userId INTEGER,
  FOREIGN KEY (userId) REFERENCES User (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (roleId) REFERENCES Role (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE S3StorageLocation
(
  id              INTEGER PRIMARY KEY NOT NULL,
  accessKeyId     TEXT                NOT NULL,
  secretAccessKey TEXT                NOT NULL,
  bucketName      TEXT                NOT NULL,
  awsRegion       TEXT                NOT NULL,
  FOREIGN KEY (id) REFERENCES StorageLocation (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE SMBStorageLocation
(
  userName   TEXT                NOT NULL,
  password   TEXT                NOT NULL,
  id         INTEGER PRIMARY KEY NOT NULL,
  hostName   TEXT                NOT NULL,
  domainName TEXT,
  FOREIGN KEY (id) REFERENCES StorageLocation (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE StorageLocation
(
  id   INTEGER PRIMARY KEY NOT NULL,
  name TEXT                NOT NULL,
  type TEXT
);

CREATE TABLE Track
(
  id          INTEGER PRIMARY KEY NOT NULL,
  trackType   TEXT                NOT NULL,
  trackNo     INTEGER             NOT NULL,
  tag         TEXT                NOT NULL,
  assetPartId INTEGER             NOT NULL,
  FOREIGN KEY (assetPartId) REFERENCES AssetPart (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE TransferEvent
(
  id                           INTEGER PRIMARY KEY NOT NULL,
  initiator                    TEXT,
  startedAt                    INTEGER             NOT NULL,
  endedAt                      INTEGER,
  originName                   TEXT,
  destinationName              TEXT,
  status                       TEXT,
  statusDetails                TEXT,
  bytesTransferred             INTEGER,
  workflowName                 TEXT,
  originStorageLocationId      INTEGER,
  destinationStorageLocationId INTEGER,
  originFileId                 INTEGER,
  destinationFileId            INTEGER,
  FOREIGN KEY (originStorageLocationId) REFERENCES StorageLocation (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (originFileId) REFERENCES File (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (id) REFERENCES RecordableEvent (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (destinationStorageLocationId) REFERENCES StorageLocation (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (destinationFileId) REFERENCES File (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE User
(
  id             INTEGER PRIMARY KEY NOT NULL,
  email          TEXT,
  username       TEXT,
  organizationId INTEGER,
  password_hash  TEXT,
  confirmed      INTEGER,
  name           TEXT,
  last_seen      TEXT,
  FOREIGN KEY (organizationId) REFERENCES Organization (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE UNIQUE INDEX ix_User_username ON User (username);
CREATE UNIQUE INDEX ix_User_email ON User (email);
CREATE TABLE ValidationEvent
(
  id                INTEGER PRIMARY KEY NOT NULL,
  initiator         TEXT,
  startedAt         INTEGER             NOT NULL,
  endedAt           INTEGER,
  name              TEXT,
  status            TEXT,
  statusDetails     TEXT,
  fileSize          INTEGER,
  workflowName      TEXT,
  validationEngine  TEXT,
  storageLocationId INTEGER,
  fileId            INTEGER,
  FOREIGN KEY (storageLocationId) REFERENCES StorageLocation (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (id) REFERENCES RecordableEvent (id)
    DEFERRABLE INITIALLY DEFERRED,
  FOREIGN KEY (fileId) REFERENCES File (id)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE VideoTrack
(
  id                   INTEGER PRIMARY KEY NOT NULL,
  frameRate            REAL,
  bitRate              INTEGER,
  aspectRatio          TEXT,
  sizeX                INTEGER,
  sizeY                INTEGER,
  videoCodec           TEXT,
  videoCodecParameters TEXT,
  noOfFrames           INTEGER,
  scanType             TEXT,
  durationMillis       INTEGER,
  FOREIGN KEY (id) REFERENCES Track (id)
    DEFERRABLE INITIALLY DEFERRED
);
