package cruds.common.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

@Component
//@Profile("prod")
public class SuperUserInitializer implements CommandLineRunner {

    private final DataSource dataSource;

    public SuperUserInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {

        InputStream scriptStream = getClass()
                .getClassLoader()
                .getResourceAsStream("scripts/init-super-user.sql");

        if (scriptStream == null) {
            System.err.println("init-super-user.sql não encontrado no classpath");
            return;
        }

        String script = new String(scriptStream.readAllBytes(), StandardCharsets.UTF_8);

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            for (String sql : script.split(";")) {
                if (!sql.trim().isEmpty()) {
                    stmt.execute(sql);
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao executar init-super-user.sql");
            e.printStackTrace();
        }
    }
}
