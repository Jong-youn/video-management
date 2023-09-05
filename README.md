# 제네시스랩 백엔드 채용 과제 : 회원 비디오 관리기능
### 작업자: 이종연(jayylee.dev@gmail.com)


### Database schema
1. 유저(user)
   - id(PK)
   - email(이메일)
   - name(이름)
   - phone_number(전화번호)
   - role_id(FK)
   - password(비밀번호)
   - created_at(가입일자)
   - updated_at(회원 정보 수정일자)
   - deleted_at(탈퇴일자)
2. 권한(role)
   - id(PK)
   - name
3. 비디오(video)
    - id(PK)
    - title
    - user_id(FK)
    - created_at(업로드 일자)
    - deleted_at(삭제일자)

### 로컬 스토리지
경로: ```src/main/resources/static/video-storage```


## 회원 가입
### API 명세
|Method| URL                | 예시                   |
|---|--------------------|----------------------|
|POST| {server-url}/users | localhost:8080/users |

### Request
- Body  
  |필드|타입|필수 여부|설명|  
  |---|---|---|---|   
  |email|String|필수|이메일|
  |password|String|필수|비밀번호|
  |name|String|선택|이름|
  |phoneNumber|String|선택|전화번호|
### Response
  |필드|타입|필수 여부|설명|  
  |---|---|---|---|   
  |data|Long|필수|유저 아이디|  


## 회원 정보 수정
### API 명세
| Method | URL                | 예시                   |
|--------|--------------------|----------------------|
| PATCH  | {server-url}/users | localhost:8080/users |
### Request
- Header  
  |파라미터|타입|필수 여부|  
  |---|---|---|   
  |Authorization|String|필수|
- Body  
  |필드|타입|필수 여부|설명|  
  |---|---|---|---|   
  |name|String|선택|이름|
  |phoneNumber|String|선택|전화번호|

## 회원 탈퇴
### API 명세
| Method | URL                | 예시                   |
|--------|--------------------|----------------------|
| DELETE | {server-url}/users | localhost:8080/users |
### Request
- Header  
  |파라미터|타입|필수 여부|  
  |---|---|---|   
  |Authorization|String|필수|

## 유저 로그인
토큰 방식으로 구현
access token 구현(refresh token은 구현하지 못했습니다)
spring security 사용
### API 명세
| Method | URL                | 예시                   |
|--------|--------------------|----------------------|
| POST | {server-url}/auth | localhost:8080/auth |
### Request
- Body  
  |필드|타입|필수 여부|설명|  
  |---|---|---|---|   
  |email|String|필수|이메일|
  |password|String|필수|비밀번호|
### Response
  |필드|타입|필수 여부|설명|  
  |---|---|---|---|   
  |accessToken|String|필수|토큰|

## 비디오 파일 업로드
로컬 디스크(src/main/resources/static/video-storage)에 저장
100mb 넘는 비디오가 업로드 됐을 경우 예외 발생

### API 명세
| Method | URL                | 예시                   |
|--------|--------------------|----------------------|
| POST | {server-url}/videos | localhost:8080/videos |
### Request
- Header  
  |파라미터|타입|필수 여부|  
  |---|---|---|   
  |Authorization|String|필수|
- Body  
  |필드|타입|필수 여부|설명|  
  |---|---|---|---|   
  |video|video|필수|비디오|
### Response
  |필드|타입|필수 여부|설명|  
  |---|---|---|---|   
  |data|Long|필수|비디오 아이디|  


## Page
### 비디오 파일 재생
URL: /videos/:videoId  
토큰 필수
- 비디오 소유자, 어드민 권한을 가진 유저만 접근 가능

