<!--
 ___ _            _ _    _ _    __
/ __(_)_ __  _ __| (_)__(_) |_ /_/
\__ \ | '  \| '_ \ | / _| |  _/ -_)
|___/_|_|_|_| .__/_|_\__|_|\__\___|
            |_| 
-->
![Logo](https://platform.simplicite.io/logos/standard/logo250.png)
* * *

`Gs1Demo` module definition
===========================

GS1 demo
=========

This module contains a demo of [GS1 standards](https://www.gs1.org) standards
within Simplicité.

Domains
-------

_Domains organize the application's main navigation menu. Each domain groups the objects, processes, and external pages accessible to users from it. Domains can be nested: a domain may have a parent domain._

* `Gs1Demo`
  * `Gs1Gtin` _(object)_
  * `Gs1Udi` _(object)_

`Gs1Gtin` (GTIN) business object definition
-------------------------------------------

`GTIN`: Internationally unique product identifier managed by the GS1 Organization

**Physical table**: `gs1_gtin`

### Relationships

* Has many **Gs1Udi** via `gs1UdiGtinId` [on delete: cascade, visible]

### Fields

| Name                                                         | Type                                     | Column                         | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|--------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `gs1GtinIdentifier`                                          | char(14)                                 | gs1_gtin_identifier            | yes*     | yes       |          | -                                                                                |
| `gs1GtinType`                                                | enum(50) using GS1_GTIN_TYPE list        | gs1_gtin_type                  | yes      | yes       |          | -                                                                                |
| `gs1GtinLabel`                                               | char(255)                                | gs1_gtin_label                 | yes      | yes       |          | -                                                                                |
| `gs1GtinEan13Image`                                          | image                                    | gs1_gtin_ean13_image           |          |           |          | -                                                                                |
| `gs1GtinUpcaImage`                                           | image                                    | gs1_gtin_upca_image            |          |           |          | -                                                                                |
| `gs1GtinItf14Image`                                          | image                                    | gs1_gtin_itf14_image           |          |           |          | -                                                                                |
| `gs1GtinDescription`                                         | html(1000000)                            | gs1_gtin_description           |          | yes       |          | -                                                                                |
| `gs1GtinPicture`                                             | image                                    | gs1_gtin_picture               |          | yes       |          | -                                                                                |

### Enumerations

* `GS1_GTIN_TYPE`
    - `MEDICAL_DEVICE` Medical device
    - `OTHER` Other

### Implemented hooks

* `postValidate`

`Gs1Udi` (UDI) business object definition
-----------------------------------------

`UDI`: A medical device universal identifier.

`UDI` = `UDI-DI` (static _device identifier_ = the GTIN) + `UDI-PI` (_production identifiers_)

**Physical table**: `gs1_udi`

### Relationships

* Belongs to **Gs1Gtin** via `gs1UdiGtinId` (required) — on parent delete: cascade

### Fields

| Name                                                         | Type                                     | Column                         | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|--------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| **`gs1UdiGtinId`** link to **`Gs1Gtin`**                     | id                                       | gs1_udi_gtin_id                | yes*     | yes       |          | -                                                                                |
| _`gs1GtinIdentifier`_                                        | _char(14)_                               | _gs1_gtin_identifier_          |          |           |          | _-_                                                                              |
| _`gs1GtinLabel`_                                             | _char(255)_                              | _gs1_gtin_label_               |          |           |          | _-_                                                                              |
| `gs1UdiProductionDate`                                       | date                                     | gs1_udi_production_date        | *        | yes       |          | -                                                                                |
| `gs1UdiExpirationDate`                                       | date                                     | gs1_udi_expiration_date        | *        | yes       |          | -                                                                                |
| `gs1UdiLotNumber`                                            | char(20)                                 | gs1_udi_lot_number             | *        | yes       |          | -                                                                                |
| `gs1UdiSerialNumber`                                         | char(20)                                 | gs1_udi_serial_number          | *        | yes       |          | -                                                                                |
| `gs1UdiSoftwareVersion`                                      | char(20)                                 | gs1_udi_software_version       | *        | yes       |          | -                                                                                |
| `gs1UdiHumanReadableInterpretation`                          | char(100)                                | gs1_udi_human_readable_interpretation |          |           |          | -                                                                                |
| `gs1UdiGs1Data`                                              | char(100)                                | gs1_udi_gs1_data               |          |           |          | -                                                                                |
| `gs1UdiDigitalLink`                                          | url(100)                                 | gs1_udi_digital_link           |          |           |          | -                                                                                |
| `gs1UdiGs1128Image`                                          | image                                    | gs1_udi_gs1128_image           |          |           |          | -                                                                                |
| `gs1UdiGs1DataMatrixImage`                                   | image                                    | gs1_udi_gs1_data_matrix_image  |          |           |          | -                                                                                |
| `gs1UdiGs1QrCodeImage`                                       | image                                    | gs1_udi_gs1_qr_code_image      |          |           |          | -                                                                                |
| `gs1UdiDigitalLinkQrCodeImage`                               | image                                    | gs1_udi_digital_link_qr_code_image |          |           |          | -                                                                                |
| `gs1UdiDescription`                                          | html(1000000)                            | gs1_udi_description            |          | yes       |          | -                                                                                |

### Implemented hooks

* `postValidate`

