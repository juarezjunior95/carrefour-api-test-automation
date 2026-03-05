# 🎯 Exemplo de Relatório Visual

## Como ficará seu relatório no GitHub Actions:

---

# 🧪 API Test Results

## 📊 Test Execution Summary

| Status | Quantity |
|--------|----------|
| ✅ **Passed** | **13** |
| ❌ **Failed** | **0** |
| ⏭️ **Skipped** | **0** |
| 📊 **Total** | **13** |

### 🎯 Success Rate: **100.0%**

⏱️ **Total Duration:** 12.64s

---

## 📋 Test Suites

| Suite | Tests | Passed | Failed | Skipped |
|-------|-------|--------|--------|---------|
| ✅ AuthTests | 3 | 3 | 0 | 0 |
| ✅ UserCrudHappyPathTest | 6 | 6 | 0 | 0 |
| ✅ UserNegativeTests | 4 | 4 | 0 | 0 |

---

## 📦 Artifacts

- 📊 **Allure Results**: Download to generate detailed HTML report
- 📄 **Surefire Reports**: XML test reports for CI/CD integration

---

## 🔍 Detalhes das Suites

### ✅ AuthTests (3 testes)
- ✅ Should login successfully and return valid JWT token
- ✅ Should return 401 when login with invalid credentials  
- ✅ Should validate that endpoints work with or without token

### ✅ UserCrudHappyPathTest (6 testes)
- ✅ Should create valid user successfully
- ✅ Should get user by ID and validate fields
- ✅ Should update user name successfully
- ✅ Should delete user successfully
- ✅ Should return 400 when getting deleted user
- ✅ CRUD Suite Summary

### ✅ UserNegativeTests (4 testes)
- ✅ Should return 400 when creating user with invalid email format
- ✅ Should return 400 when creating user without email
- ✅ Should return 400 when creating user with duplicate email
- ✅ Should return 400 when getting user with invalid ID format

---

Este relatório aparecerá automaticamente na aba **"Summary"** de cada execução do workflow! 🎉
