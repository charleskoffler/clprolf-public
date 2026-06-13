using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Windows.Input;
using Clprolf.ArchUnitNet.Attributes;
using Clprolf.Examples.WPF.Agents.Impl.SystemOriented;
using Clprolf.Examples.WPF.Entities;

namespace Clprolf.Examples.WPF.Mvvm.Impl
{
    [ClAgent]
    public class UserViewModel : IUserViewModel
    {
        public event PropertyChangedEventHandler? PropertyChanged;

        // 1. The list of Domain Agents (ObservableCollection automatically notifies UI of adds/removes)
        public ObservableCollection<User> Users { get; set; } = new();

        // 2. The currently selected Agent in your ListBox
        private User? _selectedUser;
        public User? SelectedUser
        {
            get => _selectedUser;
            set
            {
                if (_selectedUser != value)
                {
                    _selectedUser = value;
                    OnPropertyChanged(nameof(SelectedUser));

                    // Optional: When a user is selected, populate the TextBox for easy updating
                    if (_selectedUser != null)
                    {
                        NewUserName = _selectedUser.Name;
                    }
                }
            }
        }

        // 3. The text bound to your input TextBox for creation or modification
        private string _newUserName = string.Empty;
        public string NewUserName
        {
            get => _newUserName;
            set
            {
                if (_newUserName != value)
                {
                    _newUserName = value;
                    OnPropertyChanged(nameof(NewUserName));
                }
            }
        }

        // 4. Intention Agents (CRUD Behavioral Commands)
        public ICommand AddCommand { get; }
        public ICommand UpdateCommand { get; }
        public ICommand DeleteCommand { get; }

        public UserViewModel()
        {
            // Initializing commands by passing our methods
            AddCommand = new RelayCommand(ExecuteAdd, CanExecuteAdd);
            UpdateCommand = new RelayCommand(ExecuteUpdate, CanExecuteSelection);
            DeleteCommand = new RelayCommand(ExecuteDelete, CanExecuteSelection);

            // Seed some dummy data to test the ListBox on startup
            // We should have get the list from a repository.
            Users.Add(new User("Alice"));
            Users.Add(new User("Bob"));
        }

        // --- CREATE ---
        public void ExecuteAdd(object? parameter)
        {
            Users.Add(new User(NewUserName));
            NewUserName = string.Empty; // Clear the input field
            //Save here to the repository
        }
        public bool CanExecuteAdd(object? parameter) => !string.IsNullOrWhiteSpace(NewUserName);

        // --- UPDATE ---
        public void ExecuteUpdate(object? parameter)
        {
            if (SelectedUser != null)
            {
                SelectedUser.Name = NewUserName;
                // Update here with the repository
            }
        }

        // --- DELETE ---
        public void ExecuteDelete(object? parameter)
        {
            if (SelectedUser != null)
            {
                Users.Remove(SelectedUser);
                NewUserName = string.Empty; // Clear the input field
                // Delete here with the repository
            }
        }

        // Shared rule for Update and Delete: A list selection is strictly required!
        public bool CanExecuteSelection(object? parameter) => SelectedUser != null;

        private void OnPropertyChanged(string propertyName)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}