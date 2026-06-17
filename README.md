# Song System

**Sistema Completo de Gerenciamento de Repertório e Biblioteca Musical**

Plataforma para músicos organizarem bandas, álbuns, músicas, repertório de estudo, setlists e teoria musical.

---

## Principais Funcionalidades

### **Biblioteca Musical**
- Cadastro completo de **Bandas/Artistas**
- Cadastro de **Álbuns** com upload de capa
- Cadastro de **Músicas** (tom, duração, afinação, anotações)
- Visualização detalhada com relacionamentos
- Destaques na página inicial

### **Repertório**
- Adicionar músicas ao repertório pessoal
- Status: **Estudando** | **Aprendida** | **Dominada**
- Registro de práticas (data + contador)
- Dificuldade: Fácil, Médio, Difícil
- Estatísticas completas

### **Setlists**
- Criação e gerenciamento de setlists para shows
- Ordenação de músicas
- Visualização com duração estimada

### **Teoria Musical**
- **Escalas** (Maiores, Menores, Pentatônicas, Blues, etc.)
- **Acordes** com posições no braço da guitarra
- **Campos Harmônicos** por tom
- **Teorias Gerais** (conteúdos educativos)

### Outros Recursos
- Sistema de autenticação (login/cadastro)
- Upload de imagens
- Interface moderna com Bootstrap + Thymeleaf
- Filtros e buscas avançadas
- Design responsivo

---

## Como Rodar o Projeto

### Pré-requisitos
- Java 21 ou superior
- MariaDB

### Passo a passo

1. **Clone o projeto**
   ```bash
   git clone <URL-DO-SEU-REPOSITORIO>
   cd song-system
   ```

2. **Configure o banco de dados**

   Crie o banco no MariaDB:
   ```sql
   CREATE DATABASE song_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Configure `application.properties`**

   Edite o arquivo `src/main/resources/application.properties`:

   ```properties
   spring.application.name=songsystem
   spring.datasource.url=jdbc:mariadb://localhost:3306/songsystem
   spring.datasource.username=root
   spring.datasource.password=12345
   spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.properties.hibernate.show_sql=true
   
   spring.thymeleaf.prefix=classpath:/templates/
   spring.resources.static-locations=classpath:/static/
   
   spring.thymeleaf.suffix=.html
   
   app.upload.dir=C:/uploads/songsystem/
   
   spring.web.resources.static-locations=classpath:/static/,file:${app.upload.dir}
   
   spring.servlet.multipart.max-file-size=10MB
   spring.servlet.multipart.max-request-size=10MB
   ```

4. **Crie a pasta de uploads**
   ```bash
   mkdir -p uploads
   ```

5. **Execute o projeto**
   ```bash
   mvn spring-boot:run
   ```

Acesse no navegador: **http://localhost:8080**

---

## Estrutura do Projeto

```
src/main/java/com/projeto/songSystem/
├── controllers/      → Controladores MVC
├── services/         → Regras de negócio
├── repositories/     → JPA Repositories
├── models/           → Entidades do banco
├── dto/              → Objetos de transferência
└── config/           → Configurações Spring

src/main/resources/
├── templates/        → Páginas HTML (Thymeleaf)
├── static/           → CSS, JS, imagens
└── application.properties
```

## Tecnologias Utilizadas

- **Backend**: Spring Boot + Spring MVC + JPA/Hibernate
- **Frontend**: Thymeleaf + Bootstrap 5
- **Banco**: MySQL
- **Build**: Maven

---

## Telas Principais

- Dashboard / Início
- Biblioteca (Bandas, Álbuns, Músicas)
- Repertório com estatísticas
- Setlists
- Seção de Teoria Musical

---

Projeto desenvolvido como parte do Projeto Aplicado IV.
