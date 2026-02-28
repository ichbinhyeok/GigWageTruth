# Improvement Anchor - 2026-02-28

기준일: 2026-02-28  
목적: 오늘 시점의 개선 백로그를 고정(anchor)하고, 이후 변경 대비 기준선으로 사용

## 1. P0 (즉시)

### P0-1) 출처 주장 정합성 (BLS/Survey)
Status: Done (2026-02-28)  
Issue: 홈에서 `BLS.gov`, `real-world surveys`를 주장하지만 방법론/데이터 연결이 약함.  
Evidence: `src/main/jte/index.jte` L47, L53 / `src/main/jte/methodology.jte` L15  
Definition of Done: BLS/Survey 주장을 유지할 경우 페이지 내에 링크, 사용 지표 범위, 마지막 검증일을 함께 표기. 미사용이면 해당 주장 삭제.

### P0-2) Work-level Canonical 정책 미구현
Status: Done (2026-02-28)  
Issue: `selfCanonical` 필드는 있으나 실제 렌더링에서 사용되지 않음. Work-level canonical이 항상 자기 자신으로 고정됨.  
Evidence: `src/main/java/com/gigwager/model/SeoMeta.java` L5-L18 / `src/main/jte/layout/main.jte` L35 / `src/main/java/com/gigwager/controller/ProgrammaticSeoController.java` L314, L408  
Definition of Done: `selfCanonical=false` 또는 parent canonical 위임 로직이 실제 렌더링에 반영되고, 정책 테스트 추가.

### P0-3) `$unsafe` + `user_submitted` 조합 보안 리스크
Status: Done (2026-02-28)  
Issue: `user_submitted` 콘텐츠 타입이 허용되어 있고 템플릿에서 `$unsafe` 렌더링을 사용함. sanitize 파이프라인 없음.  
Evidence: `src/main/java/com/gigwager/service/CityRichContentRepository.java` L34-L37 / `src/main/jte/salary/city-work-level.jte` L166 등 / `src/main/jte/salary/city-report.jte` L164  
Definition of Done: 허용 태그/속성 기반 sanitize(예: script/event handler/javascript: 차단), 공격 패턴 테스트 케이스 추가.

### P0-4) 인코딩/문자 깨짐 정리
Status: Done (2026-02-28)  
Issue: 블로그 일부에 깨진 토큰이 존재하여 품질/신뢰 저하 가능.  
Evidence: `src/main/jte/blog/hidden-costs.jte` L51, L59, L60, L66, L86, L91, L92 / `src/main/jte/blog/uber-vs-doordash.jte` L63-L77  
Definition of Done: 깨진 문자열 0건, UTF-8 고정, CI에서 깨짐 패턴 검사.

### P0-5) FAQ Structured Data 가시성 정합성
Status: Done (2026-02-28)  
Issue: `city-report`의 FAQ JSON-LD가 화면 FAQ 섹션과 직접 매핑되지 않음.  
Evidence: `src/main/jte/salary/city-report.jte` L353-L409  
Definition of Done: JSON-LD Q/A가 사용자 화면 텍스트와 1:1로 대응되거나, 해당 스키마 제거.

### P0-6) 홈 Breadcrumb 스키마 오용 정정
Status: Done (2026-02-28)  
Issue: 홈에 단일 항목 `BreadcrumbList`를 사용해 구조화 데이터 의미가 약함.  
Evidence: `src/main/jte/index.jte`  
Definition of Done: 홈은 `WebSite` + `Organization` 스키마로 교체.

## 2. P1 (성장)

### P1-1) Sources/검증일 상단 가시화 강화
Status: Done (2026-02-28)  
Issue: Sources 블록이 하단에 위치하여 신뢰 신호 노출이 늦음.  
Evidence: `src/main/jte/salary/city-work-level.jte` L278-L292  
Definition of Done: 첫 1~2스크롤 내 `Sources` + `Last verified` 노출.

### P1-2) 소셜 메타 보강
Status: Done (2026-02-28)  
Issue: OG 기본값은 있으나 `og:url`, `twitter:*` 메타가 없음.  
Evidence: `src/main/jte/layout/main.jte` L37-L40  
Definition of Done: `og:url`, `twitter:card`, `twitter:title`, `twitter:description`, `twitter:image` 추가.

### P1-3) Title 길이 템플릿 최적화
Status: Done (2026-02-28)  
Issue: 도시/레벨 타이틀이 SERP에서 잘릴 가능성이 높음.  
Evidence: `src/main/java/com/gigwager/controller/ProgrammaticSeoController.java` L195, L306  
Definition of Done: 짧은 템플릿 도입 및 대표 도시명 샘플 길이 점검(픽셀 기준 포함).

### P1-4) 모델 차별성 보강 (wageProxy clamp)
Status: Done (2026-02-28)  
Issue: `0.9~1.2` clamp로 고비용 도시 간 값 수렴 가능.  
Evidence: `src/main/java/com/gigwager/controller/ProgrammaticSeoController.java` L479-L480  
Definition of Done: 도시 차별성을 보존하는 보정식으로 개선하고 회귀 테스트 추가.

### P1-5) 오가닉 모니터링 자동화 (Search Console 운영 보조)
Status: Done (2026-02-28)  
Issue: 상위 랜딩 페이지의 SEO/SERP/AEO 품질이 릴리스마다 흔들릴 수 있음.  
Evidence: `src/test/java/com/gigwager/OrganicMonitoringRegressionTest.java`  
Definition of Done: sitemap 상위 20 URL의 title/description/canonical/noindex/FAQ 가시성 점검 + 리포트 파일 생성.

### P1-6) CWV 튜닝 (상위 랜딩 공통)
Status: Done (2026-02-28)  
Issue: 초기 렌더에서 3rd-party 스크립트와 홈 비주얼 스크립트가 LCP/INP에 부담.  
Evidence: `src/main/jte/layout/main.jte` / `src/main/jte/index.jte`  
Definition of Done: analytics/adsense 지연 로딩 + 홈 globe 지연 초기화.

## 3. 사실 확인 메모 (앵커 보정)

### Memo-1) `robots.txt`는 이미 존재
Reference: `src/main/resources/static/robots.txt` L1-L3

### Memo-2) 월간 `lastmod`는 매월 1일 갱신 방식
Reference: `src/main/java/com/gigwager/util/AppConstants.java` / `src/main/java/com/gigwager/controller/SitemapController.java`  
Note: now pinned to `SITEMAP_LASTMOD_DATE` and updated only when material content refresh happens.

## 4. 운영 원칙 (이 앵커의 사용법)

1. 이 문서는 2026-02-28 기준선이다.
2. 신규 이슈는 다음 날짜 파일로 증분 기록한다.
3. 기존 항목 상태 변경 시 `Status`만 갱신하고 근거 라인은 유지한다.
