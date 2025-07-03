# Musinsa Backend Frontend

Musinsa Backend API를 활용하는 React 기반 프론트엔드 애플리케이션입니다.

## 기능

### 1. 대시보드
- 카테고리별 최저가 총액 표시
- 최저가 브랜드 정보
- 카테고리별 최저가 브랜드 목록

### 2. 브랜드 관리
- 새로운 브랜드 및 상품 추가
- 모든 카테고리에 1개씩 상품 등록
- 실시간 유효성 검증

### 3. 상품 분석
- 카테고리별 최저/최고가 브랜드 조회
- 시각적 가격 비교

## 기술 스택

- **React 18**: 사용자 인터페이스
- **React Router**: 페이지 라우팅
- **Styled Components**: CSS-in-JS 스타일링
- **Axios**: HTTP 클라이언트
- **React Hooks**: 상태 관리

## 설치 및 실행

### 1. 의존성 설치
```bash
cd frontend
npm install
```

### 2. 개발 서버 실행
```bash
npm start
```

### 3. 빌드
```bash
npm run build
```

## 환경 설정

### API 서버 연결
- 기본값: `http://localhost:8080`
- 환경변수 `REACT_APP_API_URL`로 변경 가능

### CORS 설정
- 개발 환경에서 `package.json`의 `proxy` 설정으로 CORS 우회
- 프로덕션에서는 API 서버에서 CORS 설정 필요

## 프로젝트 구조

```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   └── Header.js
│   ├── pages/
│   │   ├── Dashboard.js
│   │   ├── BrandManagement.js
│   │   └── ProductAnalysis.js
│   ├── services/
│   │   └── api.js
│   ├── App.js
│   └── index.js
├── package.json
└── README.md
```

## API 연동

### Product API
- `getLowestByCategory()`: 카테고리별 최저가 조회
- `getLowestBrandForAllCategories()`: 브랜드별 전체 카테고리 최저가 조회
- `getMinMaxByCategory(categoryName)`: 카테고리별 최저/최고가 조회

### Brand API
- `createBrand(brandData)`: 브랜드 생성
- `updateBrand(brandId, brandData)`: 브랜드 수정
- `deleteBrand(brandId)`: 브랜드 삭제

## 스타일링

- **반응형 디자인**: 모바일/데스크톱 호환
- **모던 UI**: 그라데이션, 그림자, 애니메이션
- **일관된 색상**: #667eea (메인 컬러)
- **사용자 친화적**: 로딩 상태, 에러 처리, 성공 메시지

## 개발 가이드

### 컴포넌트 추가
1. `src/components/` 또는 `src/pages/`에 새 파일 생성
2. Styled Components로 스타일링
3. 필요한 경우 API 서비스 추가
4. 라우팅 설정 (필요시)

### API 연동
1. `src/services/api.js`에 새 API 함수 추가
2. 컴포넌트에서 import하여 사용
3. 에러 처리 및 로딩 상태 관리

## 배포

### 정적 파일 배포
```bash
npm run build
```
- `build/` 폴더의 내용을 웹 서버에 배포

### Docker 배포
```bash
# Docker 이미지 빌드
docker build -t musinsa-frontend .

# 컨테이너 실행
docker run -p 3000:80 musinsa-frontend
``` 