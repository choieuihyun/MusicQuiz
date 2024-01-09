## 놀러가서 사용할 음악퀴즈 앱

AAC, 프레임워크, 라이브러리 최대한 사용하지 않고 제작해보는 것이 목표. 지금까지 많이 사용했으니까 불편하게도 해보는 것이 목표.

노래 때문에 저작권 걸리니까 어차피 배포가 안될 것 같음과 동시에 놀러가서 사용할 앱이기 때문에 디자인은 최대한 가난하게, 재밌게 하였음.

<p align="center"> 
  <img alt="intro" src="https://github.com/choieuihyun/MusicQuiz/assets/59135621/bfdcc6f4-f587-4cfd-adec-9dd37b410122" align="center" width="24%">
  <img alt="userInfo" src="https://github.com/choieuihyun/MusicQuiz/assets/59135621/f81ca346-f365-4923-a04b-a73996803fd9" align="center" width="24%"> 
  <img alt="playsong" src="https://github.com/choieuihyun/MusicQuiz/assets/59135621/9169fb9e-e880-4a21-805b-39fc5dc2a307" align="center" width="24%">
<figcaption align="center">왼쪽부터 실행순서</figcaption></p>




<p align="center">
  
  <img alt="userInput" src="https://github.com/choieuihyun/MusicQuiz/assets/59135621/af98e586-4783-4eb0-b383-008101bc8c4f" align="center" width="24%"> 
  <img alt="correct" src="https://github.com/choieuihyun/MusicQuiz/assets/59135621/2acdea27-9d4b-4c05-9125-abfba1001d22" align="center" width="24%">
  <img alt="ranking" src="https://github.com/choieuihyun/MusicQuiz/assets/59135621/d34f7714-8998-4ce7-bb32-2ce6029c9a93" align="center" width="24%">

<figcaption align="center">왼쪽부터 실행순서</figcaption></p>


## 앱 화면 설명

    💥 주요 기능
  
      1. 첫번째 화면(테마 선택 화면)
  
         - 원하는 테마 선택
  

      2. 두번째 화면(유저 정보 입력 화면)

         -  이름, 단체명(팀명), 팀원 수 입력

         -  입력받은 단체명을 외래키로 단체를 연결, 해당 단체에는 포함되는 유저들 리스트 저장, 단체당 최대 8명 가능합니다. (ERD 구성은 잘 모르겠음)

         -  앱을 껐다 킬 시 해당 유저 정보 그대로 입력하여 다시 진입. 로그인 기능을 필요없어 입력 상태를 유지시키진 않았습니다.


      3. 세번째 화면(노래 재생 화면)

         -  노래 재생 후 정답 입력 버튼을 눌러 정답 입력 다이얼로그 생성

         -  노래를 재생해야 5개의 문항이 보이고 정답 입력 버튼이 활성화되도록 구현했습니다.

         -  해당 단체를 기준으로 유저 정보를 입력한 순서대로 색상 배정. ex) 1번째 유저는 빨간색, 2번째 유저는 파란색
  
  
      4. 네번째 화면(정답 입력 화면)

         -  빈칸에 해당하는 정답 고른 후 다음 버튼을 눌러 다음 순서 인원에게 패스, 마지막 인원은 완료 눌러 종료

         -  다음 버튼을 누르면 해당 다이얼로그 상단 색상이 해당 유저의 색상으로 변경되도록 했습니다.


      5. 다섯번째 화면(노래 재생 화면)

         -  네번째 화면 완료 후 정답 확인, 다이얼로그에서 전달받은 정답자 목록 확인

         -  정답 확인을 버튼을 누르면 다이얼로그에서 정답을 입력한 유저들이 화면 하단에 표시되어지도록 구현하였고 해당 정답 문항이 빨간색으로 변하도록 구현.

      6. 여섯번째 화면(랭킹 화면)

         -  이름, 단체명 기준으로 가장 많이 정답을 맞춘 유저 순서대로 나열

         -  엔딩곡 재생

         -  추후 테마를 기준으로도 나눌 예정



