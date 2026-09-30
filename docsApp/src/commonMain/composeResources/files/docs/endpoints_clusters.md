# Endpoints & Clusters

The **Endpoints & Clusters** sheet shows the raw structure of a commissioned accessory, as read
from its Descriptor cluster (`0x001D`). It is the same data the app uses internally to decide which
controls to show on the device card, made visible for inspection.

This information is available for all commissioned accessories, including Unsupported Device Types,
which makes it the first place to look when a device card does not show the controls you expect.

## Opening the sheet

1. On the Dashboard, expand a device card.
2. Tap **Endpoints & Clusters**.
3. Tap **Close**, or tap outside the sheet, to return to the Dashboard.

## Endpoints

A Matter accessory is organized into endpoints. Each endpoint is an independent functional unit,
such as a single light or a single switch button, identified by a number. The sheet lists every
endpoint the accessory reports, sorted by endpoint number.

Endpoint `0` is marked **(Root)**. It is the root node endpoint that every Matter accessory
exposes, and it carries node-wide utility clusters such as Basic Information, Access Control, and
Descriptor, rather than the accessory's actual features.

If the app has not read the Descriptor cluster yet, the sheet shows
**No endpoint information available for this device yet.**

## Endpoint details

Each endpoint card groups its data into up to three sections. The number in brackets after a
section title is the number of entries in it, and sections with no entries are hidden.

| Section         | Descriptor attribute         | Description                                                                                                  |
|-----------------|------------------------------|--------------------------------------------------------------------------------------------------------------|
| Device Types    | DeviceTypeList (`0x0000`)    | The Matter device types the endpoint implements, for example On/Off Light or Door Lock.                      |
| Server Clusters | ServerList (`0x0001`)        | Clusters the endpoint hosts. Other nodes, including this app, read, write, and send commands to them.        |
| Client Clusters | ClientList (`0x0002`)        | Clusters the endpoint uses to control other nodes, for example a light switch sending On/Off commands.       |

Each entry shows its name on the left and its hex ID on the right, for example the On/Off cluster
next to `0x0006`.

## How the app uses this data

The app picks the device type shown on the device card from the device types of the non-root
endpoints, using the first one it supports. The controls on the card are then driven by the server
clusters it finds. See [Supported device types](overview.md#supported-device-types) for the
controls offered for each type.

Client clusters matter for [bindings](bindings.md): an accessory with an On/Off client cluster,
such as a light switch, can be bound to a light bulb that hosts the On/Off server cluster.
