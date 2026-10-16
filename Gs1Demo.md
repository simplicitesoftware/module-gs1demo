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
  * `Gs1UDI` _(object)_

`Gs1UDI` (Unique device identifier) business object definition
--------------------------------------------------------------

A medical device `UDI`  encoded with GS1 Application Identifiers.

`UDI` = `UDI-DI` (static _device identifier_, the GTIN) + `UDI-PI` (_production identifiers_)

**Physical table**: `gs1_udi`

### Fields

| Name                                                         | Type                                     | Column                         | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|--------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `gs1UdiDeviceIdentifier`                                     | char(14)                                 | gs1_udi_device_identifier      | yes*     | yes       |          | -                                                                                |
| `gs1UdiProductionDate`                                       | date                                     | gs1_udi_production_date        | *        | yes       |          | -                                                                                |
| `gs1UdiExpirationDate`                                       | date                                     | gs1_udi_expiration_date        | *        | yes       |          | -                                                                                |
| `gs1UdiLotNumber`                                            | char(20)                                 | gs1_udi_lot_number             | *        | yes       |          | -                                                                                |
| `gs1UdiSerialNumber`                                         | char(20)                                 | gs1_udi_serial_number          | *        | yes       |          | -                                                                                |
| `gs1UdiSoftwareVersion`                                      | char(20)                                 | gs1_udi_software_version       | *        | yes       |          | -                                                                                |
| `gs1UdiHumanReadableInterpretation`                          | char(100)                                | gs1_udi_human_readable_interpretation |          |           |          | -                                                                                |
| `gs1UdiGs1Data`                                              | char(100)                                | gs1_udi_gs1_data               |          |           |          | -                                                                                |
| `gs1UdiDigitalLink`                                          | url(100)                                 | gs1_udi_digital_link           |          |           |          | -                                                                                |
| `gs1UdiEan13Image`                                           | image                                    | gs1_udi_ean13_image            |          |           |          | -                                                                                |
| `gs1UdiGs1128Image`                                          | image                                    | gs1_udi_gs1128_image           |          |           |          | -                                                                                |
| `gs1UdiGs1DataMatrixImage`                                   | image                                    | gs1_udi_gs1_data_matrix_image  |          |           |          | -                                                                                |
| `gs1UdiGs1QrCodeImage`                                       | image                                    | gs1_udi_gs1_qr_code_image      |          |           |          | -                                                                                |
| `gs1UdiDigitalLinkQrCodeImage`                               | image                                    | gs1_udi_digital_link_qr_code_image |          |           |          | -                                                                                |

### Implemented hooks

* `postValidate`

