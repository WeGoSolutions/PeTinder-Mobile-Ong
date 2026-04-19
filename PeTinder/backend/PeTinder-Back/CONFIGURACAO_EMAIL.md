# 📧 GUIA DE CONFIGURAÇÃO DE EMAIL - PeTinder

## 🚀 Como Trocar o Email Principal

Para trocar o email usado pelo sistema, você precisa alterar **APENAS** estas variáveis no arquivo `application.properties`:

### 📝 Variáveis a Alterar:

```properties
# ========== CONFIGURAÇÃO DE EMAIL ==========
# ALTERE APENAS ESTAS VARIÁVEIS PARA TROCAR O EMAIL
app.email.from.address=SEU_NOVO_EMAIL@gmail.com
app.email.from.name=Nome da Sua Equipe
app.email.smtp.host=smtp.gmail.com
app.email.smtp.port=587
app.email.smtp.password=SUA_SENHA_DE_APP_AQUI
```

### 📋 Passos para Trocar:

1. **Substitua o email:**
   - `app.email.from.address=seunovo@email.com`

2. **Atualize o nome da equipe:**
   - `app.email.from.name=Sua Equipe`

3. **Configure a senha de app:**
   - `app.email.smtp.password=senha_de_app_do_gmail`

4. **Se não usar Gmail, ajuste o SMTP:**
   - `app.email.smtp.host=smtp.seuproveedor.com`
   - `app.email.smtp.port=587`

### 🔧 Provedores Suportados:

#### Gmail:
```properties
app.email.smtp.host=smtp.gmail.com
app.email.smtp.port=587
```

#### Outlook/Hotmail:
```properties
app.email.smtp.host=smtp-mail.outlook.com
app.email.smtp.port=587
```

#### Yahoo:
```properties
app.email.smtp.host=smtp.mail.yahoo.com
app.email.smtp.port=587
```

### ⚙️ Configuração de Senha para Gmail:

1. Acesse: https://myaccount.google.com
2. Vá em: **Segurança** → **Verificação em duas etapas** (ative)
3. Vá em: **Senhas de app**
4. Crie senha para "PeTinder"
5. Use a senha gerada em `app.email.smtp.password`

### 🧪 Modos de Teste:

#### Modo Desenvolvimento (emails simulados):
```properties
spring.profiles.active=dev
```

#### Modo Produção (emails reais):
```properties
spring.profiles.active=prod
```

### ✅ O que é Neutro (não precisa alterar):

- ✅ Templates de email são gerados automaticamente
- ✅ Remetente usa a variável configurada
- ✅ Nome da equipe nos emails usa a variável configurada
- ✅ Configurações SMTP são reutilizadas automaticamente
- ✅ Ambas V1 e V2 usam as mesmas configurações

### 🔄 Reiniciar Aplicação:

Após alterar as configurações, reinicie a aplicação para aplicar as mudanças.

---

## 📧 Emails Enviados Automaticamente:

1. **Email de Boas-vindas:** Enviado ao cadastrar usuário
2. **Notificação de Login:** Enviado a cada login
3. **Outros emails:** Todos usam as mesmas configurações centralizadas

Todos os emails são **completamente neutros** e se adaptam automaticamente às suas configurações!
