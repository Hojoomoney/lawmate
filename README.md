### 아래는 제가 진행한 프로젝트의 중요 기술 목록입니다.
- **멀티모듈화 구현**: 여러 서비스를 통합하여 멀티모듈 구조로 설계했습니다.
- **빌드 오류 트러블 슈팅**: 각 팀원의 마이크로서비스 빌드 오류를 해결하며 프로젝트의 안정성을
확보했습니다.
- **Spring Cloud Config 및 Gateway 설정**: 민감정보와 구성 파일(yaml)을 보호하기 위해 **Spring Cloud
Config** 을 사용했고, 로드밸런싱을 위해 **Spring Cloud Gateway** 와 **Eureka** 를 설정했습니다.
- **CI/CD 파이프라인 구축**: 젠킨스를 이용해 CI/CD 파이프라인을 구축하고, **GitHub webhook** 을 통해
자동으로 **jar** 파일을 생성, **Docker** 이미지를 빌드 및 배포하여 **Naver Cloud Kubernetes Service**(NKS)에
배포했습니다.
- **Kubernetes 설정**: **Kompose** 도구를 이용해 각 서비스의 **Kubernetes yaml** 파일을 통합하고, 유지보수 및
오류 격리를 위해 단일 컨테이너 파드를 설정했습니다. **Gateway** 와 **Eureka** 는 외부 접근을 위해
**LoadBalancer** 타입으로, 나머지 서비스들은 내부 접근만 가능하도록 **ClusterIP** 로 설정했습니다.
- **Webflux 와 MVC 구현**: **Spring Webflux** 를 통해 비동기 처리 방식을 구현하고, MVC 방식으로 **JPA**,
**Querydsl**, **MyBatis** 를 활용한 CRUD 기능을 개발했습니다.
- **실시간 데이터 스트림**: **Apache Kafka** 와 SSE 를 활용해 실시간 데이터 스트림 처리를 구현하여 채팅
기능을 완성했습니다.
- **데이터베이스 통합**: **MongoDB** 와 **Redis** 를 활용해 통계 데이터를 효율적으로 처리했습니다

- 
