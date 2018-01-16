## MP5: Restaurants, Queries, and Statistical Learning Design Plan

Maria Sottile and Ge (Jessica) Ma

What we know from the JSON files:

```assembly
RESTAURANTS
open: boolean (???)
url: string url to yelp profile
longitude: number 
neighborhood: array of strings ("Telegraph Ave", "UC Campus Area", "Downtown Berkeley")
business_id: string of letters, underscores, numbers (yelp-specific thing)
name: string 
categories: array of strings ("Cafe", "Restaurants", "Chinese")
state: string 
type: string ("business")
stars: number (1-5)
city: string
full_address: string 
review count: number 
photo_url: string (link to photo)
schools: array of strings (but probably will only have one, i.e. UCB)
latitude: number 
price: number (1-3)

REVIEWS
type: string ("review")
business_id: string of letters
votes: child object with fields cool, useful, funny which each have a number (number of people who reacted to each)
review_id: string of letters, numbers, underscores
text: string of the entire review
stars: number of stars given (1-5) 
user_id: same format as business id it seems
date: string (YYYY-MM-DD)

USERS
url: string (to profile of a user)
votes: same as in reviews 
review_count: number
type: string ("user")
user_id: string
name: string
average stars: number (average stars given)
```

| Yelp Types | Supertype (?) | Fields (name: type)                      | RIs and AFtions                          |
| ---------- | ------------- | ---------------------------------------- | ---------------------------------------- |
| Restaurant | Business      | profileURL: URL <br />location: Point<br />neighborhood: Set[String]<br />businessID: ID<br />name: String<br />categories: Set[String]<br />nearbySchools(?): Set[String]<br />state: String<br />stars: double<br />address: Address <br />reviewCount: int<br />photoURL: URL<br />priceRange: int | nothing is null<br />??ask about RI in general<br />AF(r) taken from JSON fields |
| YelpReview | Review        | businessID: ID<br />votes: Map[String, Integer]<br />reviewID: ID<br />content: String<br />stars: double<br />userID: ID<br />date: Date |                                          |
| YelpUser   | User          | url: URL<br />votes: Map[String, Integer]<br />reviewCount: int<br />userID: ID<br />name: String<br />averageStars: double |                                          |

Custom datatypes:

- Point: has fields for x and y coordinates
- ID: a string
- Address: has String address, String state, String city

| Supertype | Fields                                   |
| --------- | ---------------------------------------- |
| Business  | businessID: ID<br />name: String<br />category: Set[String] |
| Review**  | reviewID: ID<br />object: Business (?)<br />businessID: ID<br />content: String<br />userID: ID<br />date: Date |
| User      | userID: ID<br />name: <br />reviewCount: int |

*Is this okay?

**Should we make this more general? i.e. able to be review of a product, service, movie etc.

STATE YOUR ASSUMPTIONS: ALL BUSINESS ARE REVIEWABLE, ALL USERS ARE REVIEWERS

OUR PHILOSOPHY: MINIMIZE EXTRANESS

## Methods

- hella get methods (so many, all of them)
- creator methods
- TBD as our creativity flourishes


## The database as a whole

- fields will be the set of Restaurant, Review, and User
- store the filenames as Strings
- other methods
  - N highest rated restaurants
  - best reviews of a given restaurant
  - best reviews in general
  - best reviewer
  - much more to come with Part V!! get hyped!!