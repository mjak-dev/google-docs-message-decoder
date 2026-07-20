# Secret Message Decoder

A Java solution for decoding a hidden message from a published Google Docs document containing Unicode characters and their coordinates.

The program downloads a publicly accessible Google Doc, extracts the character and coordinate data, reconstructs the two-dimensional grid, and prints the hidden message to the console.

## Problem Statement

The input is a published Google Docs document containing rows of data in the following format:

| Character | X Coordinate | Y Coordinate |
|-----------|--------------|--------------|
| █ | 0 | 0 |
| ▀ | 1 | 0 |
| ▀ | 2 | 0 |
| ... | ... | ... |

Each row defines:

- A Unicode character
- Its **x-coordinate**
- Its **y-coordinate**

The objective is to reconstruct the complete grid by placing each character at its specified position.

Any position without a character must be filled with a space (` `).

When printed using a **monospace font**, the resulting grid reveals a hidden sequence of uppercase letters.

---

## Features

- Downloads data directly from a published Google Docs URL
- Parses Unicode characters correctly
- Automatically determines the required grid size
- Fills unspecified positions with spaces
- Prints the decoded message while preserving orientation
- Supports arbitrarily large grids

---

## Algorithm

1. Download the HTML content of the published Google Doc.
2. Parse the document and extract:
    - Character
    - X coordinate
    - Y coordinate
3. Find the maximum X and Y values.
4. Create a 2D character array initialized with spaces.
5. Place each character into its corresponding position.
6. Print the completed grid row by row.

Time Complexity:

```
O(n)
```

where **n** is the number of character entries.

Space Complexity:

```
O(width × height)
```

---

## Requirements

- Java 17 or newer
- Maven

---

## Build

```bash
mvn clean package
```

---

## Run

```bash
java -jar target/secret-message-decoder.jar
```

or execute the main class directly from your IDE.

---

## Usage

Call the decoding function with the published Google Docs URL.

```java
decodeSecretMessage(
    "https://docs.google.com/document/d/your-document-id/pub"
);
```

The program prints the decoded graphic directly to the console.

Example output:

```text
█▀▀▀
█▀▀
█
```

or, for a complete document:

```text
███   ██   ███
█  █ █  █ █
███  ████ ███
█    █  █   █
█    █  █ ███
```

---

## Project Structure

```
src
└── main
    └── java
        └── ...
            ├── Main.java
            ├── Decoder.java
            └── ...
```

---

## Notes

- The Google document must be **published to the web**.
- The program assumes the document format matches the coding exercise specification.
- Unicode block characters are fully supported.
- The output is intended to be viewed in a **fixed-width (monospace)** font.

---

## Technologies

- Java
- Maven
- Java HTTP Client (or your chosen HTTP library)
- HTML parsing library (if used)

---

## License

This project is released under the MIT License.

---

## Acknowledgements

This project is an implementation of the **Secret Message Decoder** coding exercise, which reconstructs hidden messages from coordinate-based Unicode character data published in Google Docs.