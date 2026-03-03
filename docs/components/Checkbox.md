<h1>Buttons</h1>

# LinkedCheckbox

<div>
  <img src="../images/LinkedCheckboxLight.png" alt="LinkedCheckbox previews" width=350>
</div>
<br>

## Usage

```kotlin
LinkedCheckbox(
    text = "Я согласен с политиикой хранения персоняльных данных",
    clickableTextPairs = listOf(
        Pair("политиикой хранения персоняльных данных", "http://test.test")
    ),
    checked = true,
    onCheckedChange = {},
    onClickLinkedText = {},
)
```

## Parameters

| Property           | Type                   | Default    | Description                        |
|--------------------|------------------------|------------|------------------------------------|
| modifier           | `Modifier`             | Modifier   | Модификатор                        |
| text               | `String`               | ""         | текст лейбла чекбокса              |
| clickableTextPairs | `List<TextToLinkPair>` | emptyList  | список кликабельных частей текста  |
| style              | `TextStyle`            | true       | стиль текста                       |
| linkColor          | `Color`                | systemBars |                                    |
| onCheckedChange    | `Callback`             | {}         |                                    |
| onClickLinkedText  | `Callback`             | {}         |                                    |

<br/>
