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
- **Técnicas** com nível de dificuldade
- **Teorias Gerais** (conteúdos educativos)

### Outros Recursos
- Sistema de autenticação (login/cadastro)
- Recuperação de senha por pergunta secreta
- Upload de imagens (até 5 MB por imagem)
- Interface moderna com Bootstrap + Thymeleaf
- Design responsivo

---

## Como Rodar o Projeto

### Pré-requisitos
- Java 21
- MariaDB

> O projeto já inclui o **Maven Wrapper** (`mvnw`), então não é necessário ter o Maven instalado.

### Passo a passo

1. **Clone o projeto**
   ```bash
   git clone https://github.com/EternalAwake/Projeto-IV.git
   cd Projeto-IV
   ```

2. **Crie o banco de dados**

   Abra o terminal do MariaDB e rode:
   ```sql
   CREATE DATABASE songsystem;
   ```

3. **Ajuste as credenciais do banco (se necessário)**

   Abra o arquivo `src/main/resources/application-dev.properties` e edite as três linhas abaixo conforme sua instalação local. Exemplo:

   ```properties
   spring.datasource.url=jdbc:mariadb://localhost:3306/songsystem
   spring.datasource.username=root
   spring.datasource.password=teste123
   ```

   As tabelas são criadas automaticamente na primeira execução. A pasta de uploads também é criada sozinha — não precisa criar nada na mão.

4. **Execute o projeto**

   Linux/macOS:
   ```bash
   ./mvnw spring-boot:run
   ```

   Windows:
   ```cmd
   mvnw.cmd spring-boot:run
   ```

   Na primeira execução o Maven baixa as dependências, então pode demorar um pouco. Quando aparecer `Started SongSystemApplication`, está no ar.

5. **Acesse no navegador**

   ```
   http://localhost:8080
   ```

---

## Primeiro Acesso

### Usuários comuns
Clique em **Cadastrar** na tela de login, preencha os dados e faça login normalmente.

### Usuário administrador
O sistema cria automaticamente um usuário `admin` na primeira inicialização, sem senha. No primeiro login:

1. Digite `admin` no campo de usuário e **deixe a senha em branco**
2. Clique em **Entrar** — o sistema detecta que é o primeiro acesso e redireciona para a tela de configuração
3. Defina uma senha (mínimo 8 caracteres) e cadastre uma pergunta secreta
4. Após confirmar, faça login normalmente com a senha que acabou de criar

> A tela de configuração inicial aparece apenas uma vez. A pergunta secreta é obrigatória para que o admin consiga recuperar a senha caso esqueça.

---

## Recuperação de Senha

Caso esqueça a senha, clique em **Esqueci minha senha** na tela de login e siga os passos:

1. Informe seu e-mail ou nome de usuário
2. Responda a pergunta secreta cadastrada no seu perfil
3. Defina uma nova senha

> A pergunta secreta é configurada na tela de **Meu Perfil**, na seção "Pergunta Secreta". Cadastre a sua antes de precisar — sem ela, não é possível recuperar a senha sem intervenção do administrador.

---

## Painel Administrativo

O usuário `admin` (e qualquer usuário que o admin promova) tem acesso ao painel em `/admin/usuarios`, acessível também pelo menu **Meu Perfil → Usuários**.

No painel é possível:
- Ver todos os usuários cadastrados
- **Tornar Admin** — promove um usuário comum a administrador
- **Tirar Admin** — rebaixa um admin promovido de volta a usuário comum
- **Excluir** — remove um usuário e todos os seus dados permanentemente

Regras fixas: o usuário `admin` principal não pode ser excluído nem rebaixado, e nenhum admin pode alterar a própria role ou excluir a própria conta.

---

## Ambientes (Dev / Prod)

O projeto usa perfis separados. O ambiente ativo é definido em `application.properties`, e deve ser trocado para "prod" em produção:

```properties
spring.profiles.active=dev
```

Em **produção**, nenhuma credencial fica no código — tudo é lido de variáveis de ambiente que precisam estar definidas no servidor:

| Variável | Descrição |
|---|---|
| `DB_URL` | ex.: `jdbc:mariadb://localhost:3306/songsystem` |
| `DB_USERNAME` | usuário do banco |
| `DB_PASSWORD` | senha do banco |
| `APP_UPLOAD_DIR` | diretório de uploads, ex.: `/var/songsystem/uploads/` |

---

## Estrutura do Projeto

```
src/main/java/com/projeto/songSystem/
├── controllers/      → Controladores MVC
├── services/         → Regras de negócio
├── repositories/     → JPA Repositories
├── models/           → Entidades do banco e enums
├── dto/              → Objetos de transferência
├── util/             → Upload de imagem e hash de senha
└── config/           → Segurança por sessão e recursos estáticos

src/main/resources/
├── templates/              → Páginas HTML (Thymeleaf)
├── static/                 → JS
├── application.properties          → Configurações comuns e perfil ativo
├── application-dev.properties      → Configurações de desenvolvimento
└── application-prod.properties     → Configurações de produção
```

---

## Tecnologias Utilizadas

- **Backend**: Spring Boot 4.0.3 + Spring MVC + JPA/Hibernate
- **Frontend**: Thymeleaf + Bootstrap 5
- **Banco**: MariaDB
- **Build**: Maven (via Maven Wrapper)

---

## Telas Principais

- Dashboard / Início
- Biblioteca (Bandas, Álbuns, Músicas)
- Repertório com estatísticas
- Setlists
- Seção de Teoria Musical
- Painel de administração (usuários)

---

Projeto desenvolvido como parte do Projeto Aplicado IV.
