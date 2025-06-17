 # Jobber Application - Production Docker Setup

A comprehensive microservices application with full infrastructure stack including databases, message queues, search engine, monitoring, and application services.

## 🏗️ Architecture Overview

### Core Services
- **Service Registry** (Eureka) - Service discovery and registration
- **Auth Service** - Authentication and authorization microservice

### Infrastructure Services
- **PostgreSQL** - Primary relational database
- **MySQL** - Secondary database for specific services
- **MongoDB** - Document database for flexible data storage
- **Redis** - Caching and session storage
- **RabbitMQ** - Message broker for async communication
- **Elasticsearch** - Search engine and log storage
- **Kibana** - Data visualization and log analysis

### Monitoring (Optional)
- **Prometheus** - Metrics collection
- **Grafana** - Metrics visualization and dashboards
- **Redis Commander** - Redis management interface

## 🚀 Quick Start

### Prerequisites

- Docker Engine 20.10+
- Docker Compose 2.0+
- At least 4GB RAM available for containers
- 10GB+ free disk space

### 1. Clone and Setup

```bash
# Clone your project (replace with actual repository URL)
git clone <your-repository-url>
cd jobber-app

# Copy environment template
cp .env.example .env
```

### 2. Configure Environment

Edit the `.env` file with your secure passwords and configuration:

```bash
# Edit environment variables
nano .env

# Generate secure passwords (example using openssl)
openssl rand -base64 32  # Use for database passwords
```

**⚠️ Security Note**: Always use strong, unique passwords in production!

### 3. Deploy Infrastructure

```bash
# Start infrastructure services only
docker-compose up -d postgres mysql mongodb redis rabbitmq elasticsearch

# Wait for services to be healthy (check with)
docker-compose ps

# Start remaining services
docker-compose up -d
```

### 4. Start with Monitoring (Optional)

```bash
# Include monitoring stack
docker-compose --profile monitoring up -d
```

## 📁 Directory Structure

```
.
├── docker-compose.yml           # Main compose file
├── .env                        # Environment variables
├── .env.example               # Environment template
├── services/                  # Application services
│   ├── service-registry/
│   │   └── Dockerfile
│   └── auth-service/
│       └── Dockerfile
├── config/                    # Configuration files
│   ├── elasticsearch-config/
│   ├── kibana-config/
│   ├── redis-config/
│   ├── rabbitmq-config/
│   └── mysql-config/
├── monitoring/               # Monitoring configuration
│   ├── prometheus.yml
│   └── grafana/
│       └── provisioning/
├── init-scripts/            # Database initialization
└── logs/                   # Application logs
```

## 🔧 Configuration

### Database Initialization

Create initialization scripts for databases:

```bash
# PostgreSQL initialization
mkdir -p init-scripts
echo "CREATE DATABASE IF NOT EXISTS jobber_analytics;" > init-scripts/01-init.sql

# MongoDB initialization
mkdir -p mongo-init
cat > mongo-init/01-init.js << EOF
db = db.getSiblingDB('jobber');
db.createCollection('users');
db.createCollection('jobs');
EOF
```

### Service Configuration

#### Elasticsearch Configuration
```bash
mkdir -p elasticsearch-config
cat > elasticsearch-config/elasticsearch.yml << EOF
cluster.name: jobber-cluster
node.name: jobber-node-1
path.data: /usr/share/elasticsearch/data
network.host: 0.0.0.0
http.port: 9200
discovery.type: single-node
EOF
```

#### Kibana Configuration
```bash
mkdir -p kibana-config
cat > kibana-config/kibana.yml << EOF
server.name: jobber-kibana
server.host: 0.0.0.0
elasticsearch.hosts: ["http://jobber-elasticsearch:9200"]
elasticsearch.username: "kibana_system"
elasticsearch.password: "${KIBANA_PASSWORD}"
EOF
```

### Monitoring Configuration

#### Prometheus Configuration
```bash
mkdir -p monitoring
cat > monitoring/prometheus.yml << EOF
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'service-registry'
    static_configs:
      - targets: ['jobber-service-registry:8761']
    metrics_path: '/actuator/prometheus'
  
  - job_name: 'auth-service'
    static_configs:
      - targets: ['jobber-auth-service:8081']
    metrics_path: '/actuator/prometheus'
EOF
```

## 🔍 Service Access

| Service | URL | Credentials |
|---------|-----|-------------|
| Service Registry | http://localhost:8761 | None |
| Auth Service | http://localhost:8081 | API endpoints |
| Redis Commander | http://localhost:8082 | admin / (from .env) |
| RabbitMQ Management | http://localhost:15672 | jobber / (from .env) |
| Elasticsearch | http://localhost:9200 | elastic / (from .env) |
| Kibana | http://localhost:5601 | elastic / (from .env) |
| Prometheus | http://localhost:9090 | None |
| Grafana | http://localhost:3000 | admin / (from .env) |

## 🛠️ Management Commands

### Service Management

```bash
# Start all services
docker-compose up -d

# Start specific service
docker-compose up -d postgres

# Stop all services
docker-compose down

# Stop and remove volumes (⚠️ DATA LOSS)
docker-compose down -v

# View service logs
docker-compose logs -f auth-service

# Scale a service
docker-compose up -d --scale auth-service=2
```

### Health Monitoring

```bash
# Check service health
docker-compose ps

# View resource usage
docker stats

# Check service logs
docker-compose logs --tail=100 -f
```

### Database Operations

```bash
# Connect to PostgreSQL
docker exec -it jobber-postgres psql -U postgres -d jobber_prod

# Connect to MySQL
docker exec -it jobber-mysql mysql -u root -p

# Connect to MongoDB
docker exec -it jobber-mongodb mongosh -u admin -p

# Connect to Redis
docker exec -it jobber-redis redis-cli -a your_redis_password
```

## 📊 Monitoring and Observability

### Application Metrics
- Spring Boot Actuator endpoints available at `/actuator/*`
- Prometheus metrics at `/actuator/prometheus`
- Health checks at `/actuator/health`

### Infrastructure Monitoring
- Container metrics via Docker stats
- Database performance through native tools
- Message queue monitoring via RabbitMQ Management

### Log Management
- Application logs stored in Docker volumes
- Centralized logging can be configured with ELK stack
- Log rotation configured automatically

## 🔒 Security Considerations

### Network Security
- Services isolated in custom Docker networks
- No unnecessary port exposure
- Internal service communication only

### Authentication & Authorization
- All databases require authentication
- Strong password requirements
- Service-to-service authentication via Eureka

### Data Protection
- Persistent volumes for data storage
- Regular backup recommendations
- Environment variable based secrets

## 🚢 Production Deployment

### Pre-deployment Checklist

- [ ] Update all passwords in `.env` file
- [ ] Configure resource limits based on your infrastructure
- [ ] Set up external load balancer
- [ ] Configure SSL/TLS certificates
- [ ] Set up monitoring and alerting
- [ ] Plan backup strategy
- [ ] Configure log rotation

### Scaling Guidelines

```bash
# Scale application services
docker-compose up -d --scale auth-service=3

# For database scaling, consider:
# - Read replicas for PostgreSQL/MySQL
# - MongoDB replica sets
# - Redis clustering
# - Elasticsearch cluster mode
```

### Backup Strategy

```bash
# Database backups
docker exec jobber-postgres pg_dump -U postgres jobber_prod > backup.sql
docker exec jobber-mysql mysqldump -u root -p jobber_auth > mysql_backup.sql
docker exec jobber-mongodb mongodump --host localhost --port 27017 -u admin -p

# Volume backups
docker run --rm -v jobber_postgres_data:/data -v $(pwd):/backup alpine tar czf /backup/postgres_backup.tar.gz /data
```

## 🐛 Troubleshooting

### Common Issues

1. **Services not starting**
   ```bash
   # Check logs
   docker-compose logs servicename
   
   # Check resource usage
   docker system df
   docker system prune  # Clean up if needed
   ```

2. **Database connection issues**
   ```bash
   # Verify database is running
   docker-compose ps postgres
   
   # Check database logs
   docker-compose logs postgres
   
   # Test connection
   docker exec -it jobber-postgres pg_isready -U postgres
   ```

3. **Memory issues**
   ```bash
   # Check memory usage
   docker stats --no-stream
   
   # Adjust memory limits in docker-compose.yml
   # Add under service definition:
   deploy:
     resources:
       limits:
         memory: 512M
   ```

4. **Port conflicts**
   ```bash
   # Check port usage
   netstat -tulpn | grep :8081
   
   # Change ports in .env file
   AUTH_SERVICE_PORT=8082
   ```

### Service Recovery

```bash
# Restart individual service
docker-compose restart auth-service

# Rebuild and restart
docker-compose up --build -d auth-service

# Reset service (⚠️ DATA LOSS for stateful services)
docker-compose rm -f auth-service
docker-compose up -d auth-service
```

## 📚 Additional Resources

- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [Elasticsearch Docker Setup](https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html)
- [PostgreSQL Docker Hub](https://hub.docker.com/_/postgres)

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Note**: This setup is production-ready but should be customized based on your specific requirements, infrastructure, and security policies.