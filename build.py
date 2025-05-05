import subprocess
import threading

threads = []


def build_application(app):
    print(f"Building application {app}")
    subprocess.run(f"cd {app} && gradle build -x test", shell=True)
    print(f"Application {app} finished building!")


def podman_compose_up():
    print("Running containers!")
    subprocess.run("podman-compose up --build -d", shell=True)
    print("Pipeline finished!")


def build_all_applications():
    print("Starting to build applications!")
    services = [
        "order-service",
        "orchestrator-service",
        "product-validation-service",
        "payment-service",
        "inventory-service"
    ]

    for service in services:
        thread = threading.Thread(target=build_application, args=(service,))
        threads.append(thread)
        thread.start()


def remove_remaining_containers():
    print("Removing all containers.")
    subprocess.run("podman-compose down", shell=True)

    result = subprocess.run("podman ps -aq", shell=True, capture_output=True, text=True)
    containers = result.stdout.strip().split('\n')
    containers = [c for c in containers if c]

    if containers:
        print(f"There are still {len(containers)} containers created: {containers}")
        for container in containers:
            print(f"Stopping container {container}")
            subprocess.run(f"podman container stop {container}", shell=True)
        subprocess.run("podman container prune -f", shell=True)


if __name__ == "__main__":
    print("Pipeline started!")
    build_all_applications()

    # Espera todas as threads terminarem
    for t in threads:
        t.join()

    remove_remaining_containers()
    threading.Thread(target=podman_compose_up).start()
