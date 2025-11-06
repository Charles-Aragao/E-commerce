# E-Commerce Java

Sistema de E-commerce desenvolvido em Java com interface gráfica Swing.

## Funcionalidades

### Para Clientes:
- Login e cadastro de usuários
- Navegação e busca de produtos por categoria
- Carrinho de compras com gerenciamento de itens
- Finalização de pedidos
- Histórico de pedidos
- Gerenciamento de perfil

### Para Administradores:
- Gerenciamento completo de produtos (CRUD)
- Gerenciamento de pedidos e status
- Visualização de todos os pedidos do sistema

## Estrutura do Projeto

```
src/main/java/com/ecommerce/
├── model/          # Modelos de dados (Product, User, Order, CartItem)
├── dao/            # Acesso a dados (ProductDAO, UserDAO)
├── service/        # Lógica de negócio (CartService, OrderService)
├── view/           # Interface gráfica (Swing)
├── util/           # Utilitários (SessionManager)
└── ECommerceApp.java # Classe principal
```

## Como Executar

1. Compile o projeto:
```bash
javac -d bin -cp src src/main/java/com/ecommerce/*.java src/main/java/com/ecommerce/*/*.java
```

2. Execute a aplicação:
```bash
java -cp bin com.ecommerce.ECommerceApp
```

## Usuários Padrão

### Administrador:
- Usuário: `admin`
- Senha: `admin123`

### Cliente:
- Usuário: `cliente`
- Senha: `123456`

## Tecnologias Utilizadas

- Java SE
- Swing (Interface Gráfica)
- Arquitetura MVC
- Padrão DAO
- Singleton (SessionManager)

## Recursos Implementados

- Sistema de autenticação
- Gerenciamento de sessão
- Carrinho de compras persistente durante a sessão
- Busca e filtros de produtos
- Sistema de pedidos completo
- Interface administrativa
- Validações de dados
- Tratamento de erros

## Melhorias Futuras

- Integração com banco de dados
- Sistema de pagamento
- Relatórios administrativos
- Notificações por email
- Sistema de avaliações de produtos
- Integração com APIs de frete