// Adicione um ouvinte de eventos aos botões de exclusão
document.querySelectorAll('.excluir').forEach(function(button) {
    button.addEventListener('click', function(event) {
        event.preventDefault();
        if (confirm('Confirma a exclusão?')) {
            const row = this.closest('tr'); // Obtém a linha atual da tabela
            const bandaId = this.dataset.bandaId;

            // URL COMPLETA - incluindo o caminho completo
            fetch(`/biblioteca/bandas/excluir/${bandaId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
            })
            .then(response => {
                if (response.ok) {
                    console.log('Banda excluída com sucesso.');
                    // Remove a linha da tabela após a exclusão
                    if (row) {
                        row.remove();
                    }
                    // Opcional: mostrar mensagem de sucesso
                    alert('Banda excluída com sucesso!');
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