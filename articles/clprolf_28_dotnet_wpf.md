# A Semantic Approach to WPF and MVVM with Clprolf framework

## Introduction

WPF and the MVVM (Model-View-ViewModel) pattern are powerful tools for desktop development. However, as applications grow, the technical plumbing—commands, data binding, notifications—can sometimes cloud the architectural intent. We can easily lose track of the conceptual meaning of our classes behind their technical implementations.

To bring more clarity to my desktop architectures, let's try a lightweight framework **Clprolf**. The goal is simple: use basic custom C# attributes to give every class a clear, human-readable role, and enforce these boundaries with automated architecture tests using **ArchUnitNET**.

Here is how it looks in practice.

---

## The Core Concepts: Agents and Workers

Instead of categorizing code strictly by technical layers, Clprolf looks at the application as a team of actors working together:

1. **Agents:** Core components that carry an identity, state, representation, or behavioral intention.
2. **Workers:** Quiet executors or infrastructure tools that handle background or mechanical tasks.

When applied to WPF and MVVM, the system naturally organizes into three distinct types of Agents:

* **Domain Agents (`User`):** The pure business entities holding the core data and identity.
* **Presentation Agents (`UserViewModel`):** The ambassadors between the user and the system, orchestrating data and validating user intentions.
* **System-Oriented Agents (`UserWindow` & `RelayCommand`):** Noble UI components whose purpose is to interact with and feed the hidden, native rendering engine of the .NET framework.

---

## The Code in Action

Here is a streamlined look at how a standard CRUD ViewModel expresses its semantic role through attributes and clean design:

```csharp
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Windows.Input;
using Clprolf.ArchUnitNet.Attributes;

namespace Clprolf.Example.WPF.Mvvm.Impl
{
    [ClAgent] // Marked as a Presentation Agent
    public class UserViewModel : IUserViewModel
    {
        public event PropertyChangedEventHandler? PropertyChanged;

        // ObservableCollection handles UI notification for list updates
        public ObservableCollection<User> Users { get; set; } = new();

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
                    if (_selectedUser != null) NewUserName = _selectedUser.Name;
                }
            }
        }

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

        // Intention Agents (CRUD Commands)
        public ICommand AddCommand { get; }

        public UserViewModel()
        {
            AddCommand = new RelayCommand(ExecuteAdd, CanExecuteAdd);
            
            // Initial data seeding
            // (In a full production setup, this would be fetched from a Repository Worker)
            Users.Add(new User("Alice"));
            Users.Add(new User("Bob"));
        }

        public void ExecuteAdd(object? parameter)
        {
            Users.Add(new User(NewUserName));
            NewUserName = string.Empty; 
            // Save to repository worker here...
        }
        
        public bool CanExecuteAdd(object? parameter) => !string.IsNullOrWhiteSpace(NewUserName);

        private void OnPropertyChanged(string propertyName)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}

```

## The Architecture Blueprint

To see how this language structures a real application, here is the exact layout of the WPF project. Notice how the namespaces map directly to our semantic roles rather than generic UI folders:

```text
Clprolf.Example.WPF/
│
├── App.xaml.cs                     --> [ClWorker] (Application Bootstrapper)
│
├── Entities/
│   └── User.cs                     --> [ClAgent] (Domain Identity)
│
├── Mvvm/
│   ├── IUserViewModel.cs           --> [ClFamily] [ClAgent] (Contract)
│   └── Impl/
│       └── UserViewModel.cs        --> [ClAgent] (Presentation Ambassador)
│
└── Agents/
    └── Impl/
        └── SystemOriented/
            ├── UserWindow.xaml.cs  --> [ClSystem] (UI Component)
            ├── RelayCommand.cs     --> [ClSystem] (Intention Driver)
            
	└── Traits/
		└── IClNotifyPropertyChanged.cs --> [ClTrait][ClAgent]
	
```

As you can see, every single component—from the window to the command framework—has been assigned a strict semantic responsibility.

---

## Wiring the Ecosystem Together

To understand how these Agents interact, look at how the application starts. 

First, we have our `UserWindow`, a **System-Oriented Agent** designed to serve the native WPF rendering engine by hosting the visual tree and binding the context:

```csharp
[ClSystem]
public partial class UserWindow : Window
{
    public UserWindow(UserViewModel vm)
    {
        InitializeComponent();
        DataContext = vm;
    }
}

```

Then, we have the `App.xaml.cs`. This is not an Agent; it has no business intent. It is a **Clprolf Worker**—an automated executor whose only job is to assemble the pieces, inject the dependencies, and start the factory:

```csharp
[ClWorker]
public partial class App : Application
{
    protected override void OnStartup(StartupEventArgs e)
    {
        base.OnStartup(e);

        var vm = new UserViewModel();
        var userWin = new UserWindow(vm);

        userWin.Show(); // The worker hands over control to the System Agents
    }
}

```

## Guarding the Architecture with ArchUnitNET

To ensure these semantic boundaries are never violated during development, we use automated architecture tests. For instance, we can write a rule ensuring that any interface marked as a **Trait** (a reusable structural behavior) only extends other Traits or external system interfaces, preventing illegal coupling with hard Agents.

Here is the condition that enforces this:

```csharp

[ClAgent]
internal sealed class TraitInterfacesMustExtendOnlyTraitInterfacesCondition : ICondition<Interface>
{
    public string Description => "extend only trait interfaces";

    public IEnumerable<ConditionResult> Check(IEnumerable<Interface> objects, Architecture architecture)
    {
        foreach (var interf in objects)
        {
            if (!interf.IsTrait()) { yield return new ConditionResult(interf, true, $"{interf.FullName} is ignored (Not a [ClTrait] interface)"); continue; }
            if (interf.ImplementedInterfaces.IsNullOrEmpty()) { yield return new ConditionResult(interf, true, $"{interf.FullName} has no parent interface"); continue; }

            bool allParentsAreTraitsOrExternal = interf.ImplementedInterfaces.All(parent => 
                interf.HasInterfaceBypass() || parent.IsTrait() || !parent.IsClprolf()
            );

            var faultyParent = interf.ImplementedInterfaces.FirstOrDefault(parent => 
                !interf.HasInterfaceBypass() && !parent.IsTrait() && parent.IsClprolf()
            );

            yield return new ConditionResult(
                interf, 
                allParentsAreTraitsOrExternal,
                allParentsAreTraitsOrExternal 
                    ? $"All parent interfaces of {interf.FullName} are traits or external third-party interfaces."
                    : $"{interf.FullName} is a trait interface but it inherits from {faultyParent?.FullName} which is NEITHER a trait interface NOR an external system interface"
            );
        }
    }

    public bool CheckEmpty() => true;
}

```

---

## Conclusion

By mapping the technical structure of MVVM into a simple, unified vocabulary of **Agents** and **Workers**, the cognitive load drops. The architecture stops being just about "WPF mechanics" and becomes a clear map of responsibilities.

The main benefit of this approach is consistency: whether you are looking at a desktop view-model, a web API controller, or a console application, the semantic definitions remain the same.

---
