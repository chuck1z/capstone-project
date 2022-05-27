# Api Documentation
## Endpoint
base url : ``https://not-defined.com/``
base image url : ``https://not-defined.com/static/images/
### Fruits Of The Day
- Path : ``/fruits/fotd``
- Method : ``GET``
- Response :
```json
{
    "error": false,
    "message": "Fruits of The Day fetched successfully",
    "data": {
      "fruits_id":"f0001",
      "name":"apple",
      "image":"apple.png",
      "short_desc":"lorem ipsum dolor sit amet..."
    }
}

```
### Fruits Tips
- Path : ``/fruits/tips``
- Method : ``GET``
- Response :
```json
{
    "error": false,
    "message": "Tips fetched successfully",
    "data": [
        {
            "tips_id":"tf0001",
            "title":"apple nice fruits",
            "image":"tips-apple.png",
            "short_desc":"lorem ipsum dolor sit amet..."
        },
        {
            "tips_id":"tf0001",
            "title":"orange good fruits",
            "image":"tips-orange.png",
            "short_desc":"lorem ipsum dolor sit amet..."
        }
    ]
}

```
### Fruits Tips detail
- Path : ``/fruits/tips/tf0001``
- Method : ``GET``
- Response :
```json
{
    "error": false,
    "message": "Tips detail fetched successfully",
    "data": 
        {
            "tips_id":"tf0001",
            "title":"apple nice fruits",
            "image":"tips-apple.png",
            "short_desc":"lorem ipsum dolor sit amet...",
            "date_posted":"2022-01-08T06:34:18.598Z",
            "full_desc":"orem ipsum dolor sit amet, orem ipsum dolor sit amet,orem ipsum dolor sit amet,orem ipsum dolor sit amet"

        }
        
}

```


