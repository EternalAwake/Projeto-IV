// Exclusão da banda na visualização
document.querySelectorAll('.excluir-banda').forEach(function(button) {
    button.addEventListener('click', function(event) {
        event.preventDefault();
        const bandaId = this.dataset.bandaId;
        const bandaNome = this.dataset.bandaNome;

        if (confirm(`Esta ação irá remover também todos os álbuns e músicas relacionados.`)) {
            fetch(`/biblioteca/bandas/visualizar/excluir/${bandaId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
            })
            .then(response => {
                if (response.ok) {
                    console.log('Banda excluída com sucesso.');
                    alert('Banda excluída com sucesso!');
                    // Redireciona para a listagem de bandas
                    window.location.href = '/biblioteca/bandas';
                } else {
                    console.error('Erro ao excluir banda. Status:', response.status);
                    alert('Erro ao excluir banda. Tente novamente.');
                }
            })
            .catch(error => {
                console.error('Erro de rede:', error);
                alert('Erro de rede ao excluir banda.');
            });
        }
    });
});